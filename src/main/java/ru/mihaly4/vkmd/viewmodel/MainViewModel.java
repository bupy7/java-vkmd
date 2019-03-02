package ru.mihaly4.vkmd.viewmodel;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import ru.mihaly4.vkmd.client.IVkClient;
import ru.mihaly4.vkmd.log.ILogger;
import ru.mihaly4.vkmd.model.Link;
import ru.mihaly4.vkmd.model.Target;
import ru.mihaly4.vkmd.parser.TargetParser;
import ru.mihaly4.vkmd.repository.VkRepository;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

public class MainViewModel {
    @Nonnull
    private IVkClient vkClient;
    @Nonnull
    private VkRepository vkRepository;
    @Nonnull
    private ILogger logger;
    @Nonnull
    private PublishSubject<String> status = PublishSubject.create();
    @Nonnull
    private PublishSubject<Map<String, String[]>> links = PublishSubject.create();
    @Nonnull
    private BehaviorSubject<Boolean> isLocked = BehaviorSubject.createDefault(true);

    public MainViewModel(@Nonnull IVkClient vkClient, @Nonnull VkRepository vkRepository, @Nonnull ILogger logger) {
        this.vkClient = vkClient;
        this.vkRepository = vkRepository;
        this.logger = logger;
    }

    @Nonnull
    public Boolean isLogged() {
        return vkClient.getUid() != 0;
    }

    public void parseAudioLinks(@Nonnull String url) {
        new Thread(() -> {
            isLocked.onNext(true);
            status.onNext("Parsing...");

            Target target = TargetParser.parseTarget(url);

            if (target != null) {
                if (target.isAudioType()) {
                    status.onNext("Parsing from audio...");
                    vkRepository.findAllByAudio(Integer.valueOf(target.getValue())).thenAccept(links -> {
                        status.onNext("");
                        this.links.onNext(links);
                        isLocked.onNext(false);
                    });
                }
                if (target.isWallType()) {
                    status.onNext("Parsing from wall...");
                    vkRepository.findAllByWall(target.getValue()).thenAccept(links -> {
                        status.onNext("");
                        this.links.onNext(links);
                        isLocked.onNext(false);
                    });
                }
            } else {
                status.onNext("Invalid URL");
                isLocked.onNext(false);
            }
        }).start();
    }

    public void download(@Nonnull File directory, @Nonnull Link[] links) {
        new Thread(() -> {
            isLocked.onNext(true);

            for (int i = 0; i != links.length; i++) {
                status.onNext(String.format("Downloading %d/%d...", i, links.length));

                try {
                    URL fileUrl = new URL(links[i].getUrl());
                    ReadableByteChannel rbc = Channels.newChannel(fileUrl.openStream());
                    FileOutputStream fos = new FileOutputStream(directory.getAbsolutePath()
                            + File.separator
                            + links[i].getName()
                            + ".mp3");
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();
                    rbc.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }

            status.onNext("Done!");
            isLocked.onNext(false);
        }).start();
    }

    @Nonnull
    public PublishSubject<String> getStatus() {
        return status;
    }

    @Nonnull
    public PublishSubject<Map<String, String[]>> getLinks() {
        return links;
    }

    @Nonnull
    public BehaviorSubject<Boolean> getIsLocked() {
        return isLocked;
    }

    public void unlock() {
        isLocked.onNext(false);
    }
}
