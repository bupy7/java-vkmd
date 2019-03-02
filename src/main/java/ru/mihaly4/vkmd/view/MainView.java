package ru.mihaly4.vkmd.view;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
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
import ru.mihaly4.vkmd.viewmodel.MainViewModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

public class MainView extends AbstractView {
    @Nonnull
    private MainViewModel mainViewModel;
    @Nonnull
    private LoginView loginView;
    @Nonnull
    private AboutView aboutView;
    @Nullable
    private TextField urlTxtField;
    @Nullable
    private ListView<Link> inList;
    @Nullable
    private ListView<Link> outList;
    @Nullable
    private Text statusTxt;
    @Nullable
    private Button downloadBtn;
    @Nullable
    private Button parseBtn;
    @Nullable
    private Button loginBtn;
    @Nullable
    private CompositeDisposable compositeDisposable;
    private LoginView.IHiddenObserver hiddenLoginViewObserver = () -> {
        if (mainViewModel.isLogged()) {
            loginBtn.setDisable(true);
            mainViewModel.unlock();
        }
    };

    private static final int DOUBLE_CLICK = 2;

    public MainView(
            @Nonnull Stage stage,
            @Nonnull MainViewModel mainViewModel,
            @Nonnull LoginView loginView,
            @Nonnull AboutView aboutView
    ) {
        super(stage);

        this.loginView = loginView;
        this.aboutView = aboutView;

        this.mainViewModel = mainViewModel;

        stage.setTitle(R.APP_TITLE);
        stage.getIcons().add(new Image(getClass().getResourceAsStream(R.APP_ICON)));
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
    protected void onShown(Parent root) {
        root.requestFocus();
        subscribeHandlers();
    }

    @Override
    protected void onHidden() {
        super.onHidden();
        unsubscribeHandlers();
    }

    private Node renderInput() {
        urlTxtField = new TextField();
        urlTxtField.setPromptText("URL");

        return urlTxtField;
    }

    private Node renderControl() {
        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.CENTER_LEFT);

        loginBtn = new Button("Login");
        loginBtn.setOnAction(event -> loginView.show());
        hbox.getChildren().add(loginBtn);

        parseBtn = new Button("Parse");
        parseBtn.setOnAction(event -> {
            mainViewModel.parseAudioLinks(urlTxtField.getText());
        });
        hbox.getChildren().add(parseBtn);

        downloadBtn = new Button("Download");
        downloadBtn.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose folder to save music tracks");

            File selectedDirectory = chooser.showDialog(stage);

            if (selectedDirectory != null && selectedDirectory.canWrite()) {
                mainViewModel.download(selectedDirectory, outList.getItems().toArray(new Link[0]));
            }
        });
        hbox.getChildren().add(downloadBtn);

        // @TODO: ??? I forgot why TODO...
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
        parseBtn.setDisable(true);
    }

    private void unlock() {
        downloadBtn.setDisable(false);
        parseBtn.setDisable(false);
    }

    private void subscribeHandlers() {
        compositeDisposable = new CompositeDisposable();

        Disposable disposable = mainViewModel.getStatus()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(status -> statusTxt.setText(status));
        compositeDisposable.add(disposable);

        disposable = mainViewModel.getIsLocked()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(isLocked -> {
                    if (isLocked) {
                        lock();
                    } else {
                        unlock();
                    }
                });
        compositeDisposable.add(disposable);

        disposable = mainViewModel.getLinks()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(links -> {
                    links.forEach((link, tags) -> inList.getItems()
                            .add(new Link(link, String.join(" - ", tags))));
                });
        compositeDisposable.add(disposable);

        loginView.subscribeOnHidden(hiddenLoginViewObserver);
    }

    private void unsubscribeHandlers() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
        loginView.unsubscribeOnHidden(hiddenLoginViewObserver);
    }
}
