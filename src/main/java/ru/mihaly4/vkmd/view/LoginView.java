package ru.mihaly4.vkmd.view;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.mihaly4.vkmd.viewmodel.LoginViewModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LoginView extends AbstractView {
    @Nonnull
    private LoginViewModel loginViewModel;
    @Nullable
    private Button loginBtn;
    @Nullable
    private Text statusTxt;
    @Nullable
    private VBox captchaBox;
    @Nullable
    private CompositeDisposable compositeDisposable;

    public LoginView(@Nonnull Stage stage, @Nonnull LoginViewModel loginViewModel) {
        super(stage);

        this.loginViewModel = loginViewModel;

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);

        stage.setTitle("Login");
    }

    @Override
    @Nonnull
    protected Parent onCreate() {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(5));

        TextField phoneTxtField = new TextField(loginViewModel.getUsername().getValue());
        phoneTxtField.setPromptText("Phone/Email");
        phoneTxtField.textProperty().addListener((observable, oldValue, newValue)
                -> loginViewModel.getUsername().onNext(newValue));
        vbox.getChildren().add(phoneTxtField);

        TextField passwordPassField = new PasswordField();
        passwordPassField.setPromptText("Password");
        passwordPassField.textProperty().addListener((observable, oldValue, newValue)
                -> loginViewModel.getPassword().onNext(newValue));
        vbox.getChildren().add(passwordPassField);

        // captcha
        captchaBox = new VBox(5);
        captchaBox.setManaged(false);
        captchaBox.setVisible(false);
        vbox.getChildren().add(captchaBox);

        // login box
        HBox loginBox = new HBox(5);
        loginBox.setAlignment(Pos.BASELINE_LEFT);
        loginBtn = new Button("Login");
        loginBtn.setOnAction(value -> loginViewModel.login());
        loginBox.getChildren().add(loginBtn);
        statusTxt = new Text();
        loginBox.getChildren().add(statusTxt);
        vbox.getChildren().add(loginBox);

        return vbox;
    }

    @Override
    @Nonnull
    protected Scene onStart(@Nonnull Parent root) {
        return new Scene(root, 200, 100);
    }

    @Override
    protected void onHidden() {
        super.onHidden();
        unsubscribeHandlers();
    }

    @Override
    protected void onShown(Parent root) {
        subscribeHandlers();
    }

    private void lock() {
        loginBtn.setDisable(true);
    }

    private void unlock() {
        loginBtn.setDisable(false);
    }

    private void subscribeHandlers() {
        compositeDisposable = new CompositeDisposable();

        Disposable disposable = loginViewModel.getStatus()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(status -> statusTxt.setText(status));
        compositeDisposable.add(disposable);

        disposable = loginViewModel.getIsLogin()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(isLogin -> {
                    if (isLogin) {
                        hide();
                    }
                });
        compositeDisposable.add(disposable);

        disposable = loginViewModel.getIsProcessing()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(isProcessing -> {
                    if (isProcessing) {
                        lock();
                    } else {
                        unlock();
                    }
                });
        compositeDisposable.add(disposable);

        disposable = loginViewModel.getCaptchaUrl()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(captcha -> {
                    if (captcha.isEmpty()) {
                        hideCaptcha();
                    } else {
                        showCaptcha(captcha);
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void unsubscribeHandlers() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    private void showCaptcha(String captchaUrl) {
        captchaBox.getChildren().clear();

        TextField captchaTxtField = new TextField();
        captchaTxtField.setPromptText("Captcha");
        captchaTxtField.textProperty().addListener((observable, oldValue, newValue)
                -> loginViewModel.getCaptchaCode().onNext(newValue));
        captchaBox.getChildren().add(captchaTxtField);
        ImageView captchaImg = new ImageView(new Image(captchaUrl));
        captchaBox.getChildren().add(captchaImg);

        captchaBox.setManaged(true);
        captchaBox.setVisible(true);

        stage.setHeight(205);
    }

    private void hideCaptcha() {
        captchaBox.setManaged(false);
        captchaBox.setVisible(false);

        captchaBox.getChildren().clear();

        stage.setHeight(115);
    }
}
