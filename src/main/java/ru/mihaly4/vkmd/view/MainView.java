package ru.mihaly4.vkmd.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.mihaly4.vkmd.presenter.MainPresenter;

public class MainView extends AbstractView implements IMainView {
    private TextField urlTxt = new TextField();
    private Button playStopBtn = new Button();
    private Button nextBtn = new Button();
    private Label titleLbl = new Label();

    private MainPresenter presenter = new MainPresenter();

    public MainView(Stage stage) {
        super(stage);

        presenter.bindView(this);
    }

    @Override
    public void run() {
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(5));

        urlTxt.setPromptText("Enter video url to start...");
        urlTxt.setFocusTraversable(false);
        vbox.getChildren().add(urlTxt);

        HBox hbox = new HBox(5);

        playStopBtn.setText("Play");
        hbox.getChildren().add(playStopBtn);

        nextBtn.setText("Next");
        hbox.getChildren().add(nextBtn);

        titleLbl.setText("");
        titleLbl.setAlignment(Pos.CENTER_LEFT);
        titleLbl.setPrefSize(190, 26);
        hbox.getChildren().add(titleLbl);

        vbox.getChildren().add(hbox);

        stage.setScene(new Scene(vbox, 300, 70));
        stage.setResizable(false);
        stage.show();
    }
}
