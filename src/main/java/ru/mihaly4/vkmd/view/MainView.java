package ru.mihaly4.vkmd.view;

import javafx.application.Platform;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.SystemUtils;
import ru.mihaly4.vkmd.R;
import ru.mihaly4.vkmd.model.Link;
import ru.mihaly4.vkmd.presenter.MainPresenter;

import java.io.File;

public class MainView extends AbstractView implements IMainView {
    private MainPresenter presenter;
    private LoginView loginView;
    private AboutView aboutView;
    private TextField urlTxtField;
    private ListView<Link> inList;
    private ListView<Link> outList;
    private Text statusTxt;
    private Button downloadBtn;

    private static final int DOUBLE_CLICK = 2;

    public MainView(Stage stage, MainPresenter presenter, LoginView loginView, AboutView aboutView) {
        super(stage);

        this.loginView = loginView;
        this.aboutView = aboutView;

        presenter.bindView(this);
        this.presenter = presenter;

        stage.setTitle(R.APP_TITLE);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(R.APP_ICON)));
    }

    @Override
    public void setStatus(String status) {
        Platform.runLater(() -> statusTxt.setText(status));
    }

    @Override
    protected Parent onCreate() {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(5));

        vbox.getChildren().addAll(renderInput(), renderControl(), renderList(vbox));

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

    private Node renderInput() {
        urlTxtField = new TextField();
        urlTxtField.setPromptText("URL");

        return urlTxtField;
    }

    private Node renderControl() {
        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.CENTER_LEFT);

        Button parseBtn = new Button("Parse");
        parseBtn.setOnAction(event -> {
            if (!presenter.isLogged()) {
                loginView.show(true); // @TODO: Replace to Event Bus
            }
            // @TODO: Replace to Event Bus
            if (presenter.isLogged()) {
                presenter.parseAudioLinks(urlTxtField.getText())
                        .thenAccept(links
                                -> links.forEach((link, tags)
                                        -> inList.getItems().add(new Link(link, String.join(" - ", tags))))
                        );
            }
        });
        hbox.getChildren().add(parseBtn);

        downloadBtn = new Button("Download");
        downloadBtn.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose folder to save music tracks");

            File selectedDirectory = chooser.showDialog(stage);

            if (selectedDirectory != null && selectedDirectory.canWrite()) {
                lock();
                presenter.download(selectedDirectory, outList.getItems().toArray(new Link[0])).thenAccept(result -> {
                    unlock();
                });
            }
        });
        hbox.getChildren().add(downloadBtn);

        // @TODO
        if (!SystemUtils.IS_OS_MAC) {
            Button aboutBtn = new Button("?");
            aboutBtn.setOnAction(event -> {
                aboutView.show();
            });
            hbox.getChildren().add(aboutBtn);
        }

        statusTxt = new Text();
        hbox.getChildren().add(statusTxt);

        return hbox;
    }

    private Node renderList(Region parent) {
        HBox hbox = new HBox(5);
        hbox.prefHeightProperty().bind(parent.heightProperty());

        inList = new ListView<>();
        inList.prefWidthProperty().bind(hbox.widthProperty());
        inList.setOnMouseClicked(event -> {
            if (event.getClickCount() == DOUBLE_CLICK) {
                Link selected = inList.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    return;
                }

                inList.getItems().remove(selected);
                outList.getItems().add(selected);
            }
        });
        hbox.getChildren().add(inList);

        VBox vbox = new VBox(5);
        vbox.setMinWidth(35);

        Button allToOutBtn = new Button();
        allToOutBtn.setText(">>");
        allToOutBtn.setOnAction(event -> {
            outList.getItems().setAll(inList.getItems().toArray(new Link[0]));
            inList.getItems().clear();
        });
        vbox.getChildren().add(allToOutBtn);

        Button clearOutBtn = new Button();
        clearOutBtn.setText("<<");
        clearOutBtn.setOnAction(event -> {
            inList.getItems().setAll(outList.getItems().toArray(new Link[0]));
            outList.getItems().clear();
        });
        vbox.getChildren().add(clearOutBtn);

        hbox.getChildren().add(vbox);

        outList = new ListView<>();
        outList.prefWidthProperty().bind(hbox.widthProperty());
        outList.setOnMouseClicked(event -> {
            if (event.getClickCount() == DOUBLE_CLICK) {
                Link selected = outList.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    return;
                }

                outList.getItems().remove(selected);
                inList.getItems().add(selected);
            }
        });
        hbox.getChildren().add(outList);

        return hbox;
    }

    private void lock() {
        downloadBtn.setDisable(true);
    }

    private void unlock() {
        Platform.runLater(() -> downloadBtn.setDisable(false));
    }
}
