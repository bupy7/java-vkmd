package ru.mihaly4.vkmd.repository;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import ru.mihaly4.vkmd.client.IVkClient;
import ru.mihaly4.vkmd.decoder.VkMusicLinkDecoder;
import ru.mihaly4.vkmd.log.ILogger;

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

        repository.findAllByProfile(123456789).thenAccept(action -> {
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

        repository.findAllByCommunity("example").thenAccept(action -> {
            links.putAll(action);

            signal.countDown();
        });

        signal.await();

        assertEquals(7, links.size());
    }

    private static class VkClient implements IVkClient {
        @Override
        public String fromProfile(int id, int offset) {
            if (offset != 0) {
                return "";
            }

            URL fixture = getClass().getResource("/fixture/profile.html");

            try {
                return Resources.toString(fixture, Charsets.UTF_8);
            } catch (IOException e) {
                return "";
            }
        }

        @Override
        public String fromCommunity(String id, int offset) {
            if (offset != 0) {
                return "";
            }

            URL fixture = getClass().getResource("/fixture/community.html");

            try {
                return Resources.toString(fixture, Charsets.UTF_8);
            } catch (IOException e) {
                return "";
            }
        }

        @Override
        public String getRemixSid() {
            return "";
        }

        @Override
        public int getUid() {
            return 444529088; // need for to decode
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