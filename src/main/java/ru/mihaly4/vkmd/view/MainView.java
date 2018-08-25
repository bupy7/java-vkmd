package ru.mihaly4.vkmd.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.mihaly4.vkmd.R;
import ru.mihaly4.vkmd.presenter.MainPresenter;

public class MainView extends AbstractView implements IMainView {
    private MainPresenter presenter = new MainPresenter();

    public MainView(Stage stage) {
        super(stage);

        presenter.bindView(this);

        stage.setTitle(R.TITLE_APP);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(R.ICON_APP)));
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
        TextField urlTxt = new TextField();
        urlTxt.setPromptText("Enter URL");

        return urlTxt;
    }

    private Node controlRender() {
        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.CENTER_LEFT);

        Button parseBtn = new Button();
        parseBtn.setText("Parse");
        hbox.getChildren().add(parseBtn);

        Button downloadBtn = new Button();
        downloadBtn.setText("Download");
        hbox.getChildren().add(downloadBtn);

        Label statusLbl = new Label();
        hbox.getChildren().add(statusLbl);

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
