package ru.mihaly4.vkmd.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.mihaly4.vkmd.R;
import ru.mihaly4.vkmd.presenter.MainPresenter;

public class MainView extends AbstractView implements IMainView {
    private MainPresenter presenter;
    private LoginView loginView;

    public MainView(Stage stage, MainPresenter presenter, LoginView loginView) {
        super(stage);

        this.loginView = loginView;

        presenter.bindView(this);
        this.presenter = presenter;

        stage.setTitle(R.APP_TITLE);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(R.APP_ICON)));
    }

    @Override
    protected Parent onCreate() {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(5));

        vbox.getChildren().addAll(inputRender(), controlRender(), listRender(vbox));

        return vbox;
    }

    @Override
    protected void onStart(Parent root) {
        Scene scene = new Scene(root, 485, 400);

        stage.setScene(scene);
    }

    @Override
    protected void onResume(Parent root) {
        root.requestFocus();
    }

    private Node inputRender() {
        TextField urlTxtField = new TextField();
        urlTxtField.setPromptText("URL");

        return urlTxtField;
    }

    private Node controlRender() {
        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.CENTER_LEFT);

        Button parseBtn = new Button();
        parseBtn.setText("Parse");
        parseBtn.setOnAction(value -> {
            if (!presenter.isLogged()) {
                loginView.show();
            }
        });
        hbox.getChildren().add(parseBtn);

        Button downloadBtn = new Button();
        downloadBtn.setText("Download");
        hbox.getChildren().add(downloadBtn);

        Text statusTxt = new Text();
        hbox.getChildren().add(statusTxt);

        return hbox;
    }

    private Node listRender(Region parent) {
        HBox hbox = new HBox(5);
        hbox.prefHeightProperty().bind(parent.heightProperty());

        ListView inList = new ListView();
        inList.prefWidthProperty().bind(hbox.widthProperty());
        hbox.getChildren().add(inList);

        VBox vbox = new VBox(5);
        vbox.setMinWidth(35);

        Button allToOutBtn = new Button();
        allToOutBtn.setText(">>");
        vbox.getChildren().add(allToOutBtn);

        Button clearOutBtn = new Button();
        clearOutBtn.setText("<<");
        vbox.getChildren().add(clearOutBtn);

        hbox.getChildren().add(vbox);

        ListView outList = new ListView();
        outList.prefWidthProperty().bind(hbox.widthProperty());
        hbox.getChildren().add(outList);

        return hbox;
    }
}
