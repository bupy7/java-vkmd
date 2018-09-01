package ru.mihaly4.vkmd.di;

import ru.mihaly4.vkmd.client.VkClient;
import ru.mihaly4.vkmd.decoder.VkMusicLinkDecoder;
import ru.mihaly4.vkmd.log.MemoryLogger;
import ru.mihaly4.vkmd.menu.MainMenu;
import ru.mihaly4.vkmd.presenter.LoginPresenter;
import ru.mihaly4.vkmd.presenter.MainPresenter;
import ru.mihaly4.vkmd.repository.VkRepository;
import ru.mihaly4.vkmd.view.AboutView;
import ru.mihaly4.vkmd.view.LoginView;
import ru.mihaly4.vkmd.view.MainView;
import dagger.Module;
import dagger.Provides;
import javafx.stage.Stage;

import javax.inject.Singleton;

@Module
public class ApplicationModule {
    private Stage primaryStage;

    public ApplicationModule(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Provides
    @Singleton
    public MainView provideMainView(MainPresenter mainPresenter, LoginView loginView) {
        return new MainView(primaryStage, mainPresenter, loginView);
    }

    @Provides
    @Singleton
    public MainMenu provideMainMenu(AboutView aboutView) {
        return new MainMenu(aboutView);
    }

    @Provides
    @Singleton
    public VkMusicLinkDecoder provideVkMusicLinkDecoder() {
        return new VkMusicLinkDecoder();
    }

    @Provides
    @Singleton
    public VkRepository prodideVkRepository(
            VkClient vkClient,
            VkMusicLinkDecoder vkMusicLinkDecoder,
            MemoryLogger logger
    ) {
        return new VkRepository(vkClient, vkMusicLinkDecoder, logger);
    }

    @Provides
    @Singleton
    public VkClient provideVkClient() {
        return new VkClient();
    }

    @Provides
    @Singleton
    public MemoryLogger provideMemoryLogger() {
        return new MemoryLogger();
    }

    @Provides
    @Singleton
    public AboutView provideAboutView() {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        return new AboutView(stage);
    }

    @Provides
    @Singleton
    public LoginView provideLoginView(LoginPresenter loginPresenter) {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);

        return new LoginView(stage, loginPresenter);
    }

    @Provides
    public MainPresenter provideMainPresenter(VkClient vkClient, VkRepository vkRepository) {
        return new MainPresenter(vkClient, vkRepository);
    }

    @Provides
    public LoginPresenter provideLoginPresenter(VkClient vkClient) {
        return new LoginPresenter(vkClient);
    }
}
