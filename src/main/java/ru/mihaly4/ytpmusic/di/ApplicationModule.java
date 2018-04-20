package ru.mihaly4.ytpmusic.di;

import ru.mihaly4.ytpmusic.menu.MainMenu;
import ru.mihaly4.ytpmusic.view.MainView;
import dagger.Module;
import dagger.Provides;
import javafx.stage.Stage;

@Module
public class ApplicationModule {
    private Stage primaryStage;

    public ApplicationModule(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Provides
    @SharedScope
    public MainView provideMainView() {
        return new MainView(primaryStage);
    }

    @Provides
    @SharedScope
    public MainMenu provideMainMenu() {
        return new MainMenu();
    }
}
