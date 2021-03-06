package ru.mihaly4.vkmd;

import ru.mihaly4.vkmd.di.ApplicationComponent;
import ru.mihaly4.vkmd.di.ApplicationModule;
import ru.mihaly4.vkmd.di.DaggerApplicationComponent;
import javafx.application.Application;
import javafx.stage.Stage;

public class Bootstrap extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationComponent di = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(primaryStage))
                .build();

        // show main view
        di.makeMainView().show();

        // render menu
        di.makeMainMenu().render();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
