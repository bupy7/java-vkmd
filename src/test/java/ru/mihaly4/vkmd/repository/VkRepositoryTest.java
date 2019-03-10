package ru.mihaly4.vkmd.repository;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import ru.mihaly4.vkmd.client.IVkClient;
import ru.mihaly4.vkmd.decoder.VkMusicLinkDecoder;
import ru.mihaly4.vkmd.log.ILogger;
import ru.mihaly4.vkmd.model.LoginResponse;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class VkRepositoryTest {
    @Test
    public void getFromProfile() throws InterruptedException {
        VkClient client = new VkClient();
        VkRepository repository = new VkRepository(client, new VkMusicLinkDecoder(), new DummyLogger());
        final Map<String, String[]> links = new HashMap<>();

        final CountDownLatch signal = new CountDownLatch(1);

        repository.findAllByAudio(123456789).thenAccept(action -> {
            links.putAll(action);

            signal.countDown();
        });

        signal.await();

        assertEquals(48, links.size());
    }

    @Test
    public void getFromCommunity() throws InterruptedException {
        VkClient client = new VkClient();
        VkRepository repository = new VkRepository(client, new VkMusicLinkDecoder(), new DummyLogger());
        final Map<String, String[]> links = new HashMap<>();

        final CountDownLatch signal = new CountDownLatch(1);

        repository.findAllByWall("example").thenAccept(action -> {
            links.putAll(action);

            signal.countDown();
        });

        signal.await();

        assertEquals(7, links.size());
    }

    private static class VkClient implements IVkClient {
        @Override
        @Nonnull
        public String fromAudio(int id, int offset) {
            if (offset != 0) {
                return "";
            }

            URL fixture = getClass().getResource("/fixture/audio.html");

            try {
                return Resources.toString(fixture, Charsets.UTF_8);
            } catch (IOException e) {
                return "";
            }
        }

        @Override
        @Nonnull
        public String fromWall(String id, int offset) {
            if (offset != 0) {
                return "";
            }

            URL fixture = getClass().getResource("/fixture/wall.html");

            try {
                return Resources.toString(fixture, Charsets.UTF_8);
            } catch (IOException e) {
                return "";
            }
        }

        @Override
        @Nonnull
        public LoginResponse login(@Nonnull String username, @Nonnull String password, @Nonnull String captchaCode) {
            return new LoginResponse(true);
        }

        @Override
        public int getUid() {
            return 444529088;
        }
    }

    private static class DummyLogger implements ILogger {
        @Override
        public void error(String message) {
            // nothing
        }

        @Override
        public void info(String message) {
            // nothing
        }
    }
}
