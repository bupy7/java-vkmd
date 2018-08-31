package ru.mihaly4.vkmd.view;

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
import ru.mihaly4.vkmd.presenter.LoginPresenter;

public class LoginView extends AbstractView implements ILoginView {
    private LoginPresenter presenter;
    private Button loginBtn;
    private Text statusTxt;

    public LoginView(Stage stage, LoginPresenter presenter) {
        super(stage);

        presenter.bindView(this);
        this.presenter = presenter;

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
            presenter.login(phoneTxtField.getText(), passwordPassField.getText()).thenAccept(action -> {
                unlock();
                if (action) {
                    Platform.runLater(stage::close);
                }
            });
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

    private void lock() {
        loginBtn.setDisable(true);
    }

    private void unlock() {
        Platform.runLater(() -> loginBtn.setDisable(false));
    }
}
