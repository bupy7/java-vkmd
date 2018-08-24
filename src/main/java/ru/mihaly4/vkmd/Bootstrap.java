package ru.mihaly4.vkmd;

import ru.mihaly4.vkmd.di.ApplicationComponent;
import ru.mihaly4.vkmd.di.ApplicationModule;
import ru.mihaly4.vkmd.di.DaggerApplicationComponent;
import javafx.application.Application;
import javafx.stage.Stage;
import ru.mihaly4.vkmd.view.AbstractView;

public class Bootstrap extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationComponent di = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(primaryStage))
                .build();

        // run view
        AbstractView mainView = di.makeMainView();
        mainView.create();
        mainView.start();

        // configuration menu
        di.makeMainMenu().render();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
