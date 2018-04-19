package ru.mihaly4.ytpmusic;

import ru.mihaly4.ytpmusic.di.ApplicationModule;
import ru.mihaly4.ytpmusic.di.DaggerApplicationComponent;
import ru.mihaly4.ytpmusic.view.MainView;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Bootstrap extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // initial configuration
        primaryStage.setTitle("YouTube Music Player");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(R.ICON_YOUTUBE)));

        // run
        MainView mainView = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(primaryStage))
                .build()
                .makeMainView();

        mainView.render();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
