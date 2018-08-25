package ru.mihaly4.vkmd.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AboutView extends AbstractView implements IAboutView {
    public AboutView(Stage stage) {
        super(stage);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
    }

    @Override
    protected Parent onCreate() {
        return new Label("Example");
    }

    @Override
    protected void onStart(Parent root) {
        stage.setScene(new Scene(root, 150, 150));
    }
}
