package ru.mihaly4.vkmd;

import ru.mihaly4.vkmd.di.ApplicationComponent;
import ru.mihaly4.vkmd.di.ApplicationModule;
import ru.mihaly4.vkmd.di.DaggerApplicationComponent;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Bootstrap extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // initial configuration
        primaryStage.setTitle("VK Music Downloader");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(R.ICON_APP)));

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
