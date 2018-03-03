package com.iamrusty.ytpmusic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Bootstrap extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("YouTube Music Player");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(R.ICON_YOUTUBE)));
        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 300, 200));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
