package ru.mihaly4.vkmd.presenter;

import ru.mihaly4.vkmd.client.IVkClient;
import ru.mihaly4.vkmd.log.ILogger;
import ru.mihaly4.vkmd.model.Link;
import ru.mihaly4.vkmd.model.Target;
import ru.mihaly4.vkmd.parser.TargetParser;
import ru.mihaly4.vkmd.repository.VkRepository;
import ru.mihaly4.vkmd.view.IMainView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MainPresenter extends AbstractPresenter<IMainView> {
    private IVkClient vkClient;
    private VkRepository vkRepository;
    private ILogger logger;

    public MainPresenter(IVkClient vkClient, VkRepository vkRepository, ILogger logger) {
        this.vkClient = vkClient;
        this.vkRepository = vkRepository;
        this.logger = logger;
    }

    public Boolean isLogged() {
        return vkClient.getUid() != 0;
    }

    public CompletableFuture<Map<String, String[]>> parseAudioLinks(String url) {
        view.setStatus("Parsing...");

        Target target = TargetParser.parseTarget(url);

        if (target != null) {
            if (target.isAudioType()) {
                view.setStatus("Parsing from audio...");
                return vkRepository.findAllByAudio(Integer.valueOf(target.getValue())).thenApplyAsync(links -> {
                    view.setStatus("");

                    return links;
                });
            }
            if (target.isWallType()) {
                view.setStatus("Parsing from wall...");
                return vkRepository.findAllByWall(target.getValue()).thenApplyAsync(links -> {
                    view.setStatus("");

                    return links;
                });
            }
        }

        view.setStatus("Invalid URL");

        return CompletableFuture.supplyAsync(HashMap::new);
    }

    public CompletableFuture<Boolean> download(File directory, Link[] links) {
        return CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i != links.length; i++) {
                view.setStatus(String.format("Downloading %d/%d...", i, links.length));

                try {
                    URL fileUrl = new URL(links[i].getUrl());
                    ReadableByteChannel rbc = Channels.newChannel(fileUrl.openStream());
                    FileOutputStream fos = new FileOutputStream(directory.getAbsolutePath()
                            + "/" + links[i].getName()
                            + ".mp3");
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();
                    rbc.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }

            view.setStatus("Done!");

            return true;
        });
    }
}
