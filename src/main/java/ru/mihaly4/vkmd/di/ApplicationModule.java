package ru.mihaly4.vkmd.di;

import dagger.Module;
import dagger.Provides;
import javafx.stage.Stage;
import ru.mihaly4.vkmd.client.VkClient;
import ru.mihaly4.vkmd.decoder.VkMusicLinkDecoder;
import ru.mihaly4.vkmd.log.MemoryLogger;
import ru.mihaly4.vkmd.menu.MainMenu;
import ru.mihaly4.vkmd.repository.VkRepository;
import ru.mihaly4.vkmd.view.AboutView;
import ru.mihaly4.vkmd.view.LoginView;
import ru.mihaly4.vkmd.view.MainView;
import ru.mihaly4.vkmd.viewmodel.LoginViewModel;
import ru.mihaly4.vkmd.viewmodel.MainViewModel;

import javax.inject.Singleton;

@Module
public class ApplicationModule {
    private Stage primaryStage;

    public ApplicationModule(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Provides
    @Singleton
    public MainView provideMainView(MainViewModel mainViewModel, LoginView loginView, AboutView aboutView) {
        return new MainView(primaryStage, mainViewModel, loginView, aboutView);
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
    public LoginView provideLoginView(LoginViewModel loginViewModel) {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);

        return new LoginView(stage, loginViewModel);
    }

    @Provides
    public MainViewModel provideMainViewModel(VkClient vkClient, VkRepository vkRepository, MemoryLogger logger) {
        return new MainViewModel(vkClient, vkRepository, logger);
    }

    @Provides
    public LoginViewModel provideLoginViewModel(VkClient vkClient) {
        return new LoginViewModel(vkClient);
    }
}
