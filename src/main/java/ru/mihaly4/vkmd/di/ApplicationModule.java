package ru.mihaly4.vkmd.di;

import ru.mihaly4.vkmd.client.VkClient;
import ru.mihaly4.vkmd.config.PackageConfig;
import ru.mihaly4.vkmd.decoder.VkMusicLinkDecoder;
import ru.mihaly4.vkmd.log.MemoryLogger;
import ru.mihaly4.vkmd.menu.MainMenu;
import ru.mihaly4.vkmd.repository.VkRepository;
import ru.mihaly4.vkmd.view.AboutView;
import ru.mihaly4.vkmd.view.MainView;
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
    public MainMenu provideMainMenu(AboutView aboutView) {
        return new MainMenu(aboutView);
    }

    @Provides
    @SharedScope
    public VkMusicLinkDecoder provideVkMusicLinkDecoder() {
        return new VkMusicLinkDecoder();
    }

    @Provides
    @SharedScope
    public VkRepository prodideVkRepository(
            VkClient vkClient,
            VkMusicLinkDecoder vkMusicLinkDecoder,
            MemoryLogger logger
    ) {
        return new VkRepository(vkClient, vkMusicLinkDecoder, logger);
    }

    @Provides
    @SharedScope
    public VkClient provideVkClient(PackageConfig config) {
        return new VkClient(config.getVkRemixSid(), config.getVkUid());
    }

    @Provides
    @SharedScope
    public PackageConfig providePackageConfig() {
        return new PackageConfig();
    }

    @Provides
    @SharedScope
    public MemoryLogger provideMemoryLogger() {
        return new MemoryLogger();
    }

    @Provides
    @SharedScope
    public AboutView provideAboutView() {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        return new AboutView(stage);
    }
}
