package ru.mihaly4.ytpmusic;

import ru.mihaly4.ytpmusic.di.ApplicationComponent;
import ru.mihaly4.ytpmusic.di.ApplicationModule;
import ru.mihaly4.ytpmusic.di.DaggerApplicationComponent;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Bootstrap extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // initial configuration
        primaryStage.setTitle("YTP Music");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(R.ICON_YOUTUBE)));

        ApplicationComponent di = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(primaryStage))
                .build();

        // run view
        di.makeMainView().run();

        // configuration menu
        di.makeMainMenu().run();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
