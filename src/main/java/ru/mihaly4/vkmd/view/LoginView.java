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
    private CompositeDisposable compositeDisposable;

    public LoginView(@Nonnull Stage stage, @Nonnull LoginViewModel loginViewModel) {
        super(stage);

        this.loginViewModel = loginViewModel;

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);

        stage.setTitle("Login");
    }

    @Override
    protected Parent onCreate() {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(5));

        TextField phoneTxtField = new TextField();
        phoneTxtField.setPromptText("Phone number");
        vbox.getChildren().add(phoneTxtField);

        TextField passwordPassField = new PasswordField();
        passwordPassField.setPromptText("Password");
        vbox.getChildren().add(passwordPassField);

        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.BASELINE_LEFT);

        loginBtn = new Button("Login");
        loginBtn.setOnAction(value -> loginViewModel.login(phoneTxtField.getText(), passwordPassField.getText()));
        hbox.getChildren().add(loginBtn);

        statusTxt = new Text();
        hbox.getChildren().add(statusTxt);

        vbox.getChildren().add(hbox);

        return vbox;
    }

    @Override
    protected void onStart(Parent root) {
        stage.setScene(new Scene(root, 200, 100));
    }

    @Override
    protected void onHidden() {
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
                        stage.close();
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
    }

    private void unsubscribeHandlers() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }
}
