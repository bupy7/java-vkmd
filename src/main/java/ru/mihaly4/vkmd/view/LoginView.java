package ru.mihaly4.vkmd.view;

import io.reactivex.disposables.Disposable;
import javafx.application.Platform;
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
import java.util.ArrayList;
import java.util.List;

public class LoginView extends AbstractView implements ILoginView {
    @Nonnull
    private LoginViewModel loginViewModel;
    @Nullable
    private Button loginBtn;
    @Nullable
    private Text statusTxt;
    @Nonnull
    private List<Disposable> subscribers = new ArrayList<>();

    public LoginView(@Nonnull Stage stage, @Nonnull LoginViewModel loginViewModel) {
        super(stage);

        this.loginViewModel = loginViewModel;

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);

        stage.setTitle("Login");
    }

    @Override
    public void setStatus(String status) {
        Platform.runLater(() -> statusTxt.setText(status));
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
        loginBtn.setOnAction(value -> {
            lock();
            loginViewModel.login(phoneTxtField.getText(), passwordPassField.getText());
        });
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
        Platform.runLater(() -> loginBtn.setDisable(false));
    }

    private void subscribeHandlers() {
        Disposable disposable = loginViewModel.getStatus().subscribe(status -> {
            statusTxt.setText(status);
        });
        subscribers.add(disposable);

        disposable = loginViewModel.getIsLogin().subscribe(isLogin -> {
            unlock();
            if (isLogin) {
                Platform.runLater(stage::close);
            }
        });
        subscribers.add(disposable);
    }

    private void unsubscribeHandlers() {
        for (int i = 0; i != subscribers.size(); i++) {
            subscribers.get(i).dispose();
        }
        subscribers.clear();
    }
}
