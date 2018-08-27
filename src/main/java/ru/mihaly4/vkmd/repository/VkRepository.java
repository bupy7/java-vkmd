package ru.mihaly4.vkmd.repository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.mihaly4.vkmd.client.IVkClient;
import ru.mihaly4.vkmd.decoder.VkMusicLinkDecoder;
import ru.mihaly4.vkmd.log.ILogger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class VkRepository {
    private static final int AUDIO_LIMIT = 50;
    private static final int WALL_LIMIT = 5;
    private static final long DDOS_DELAY = 1000;

    private IVkClient client;
    private VkMusicLinkDecoder linkDecoder;
    private ILogger logger;

    public VkRepository(IVkClient client, VkMusicLinkDecoder linkDecoder, ILogger logger) {
        this.client = client;
        this.linkDecoder = linkDecoder;
        this.logger = logger;
    }

    public CompletableFuture<Map<String, String[]>> findAllByWall(String id) {
        return findAll(page -> client.fromWall(id, WALL_LIMIT * page));
    }

    public CompletableFuture<Map<String, String[]>> findAllByAudio(int id) {
        return findAll(page -> client.fromAudio(id, AUDIO_LIMIT * page));
    }

    private CompletableFuture<Map<String, String[]>> findAll(IFetcher fetcher) {
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, String[]> links = new HashMap<>();
            int oldLinkSize = 0;
            int page = 0;

            do {
                oldLinkSize = links.size();

                Document doc = Jsoup.parse(fetcher.fetch(page++));

                Elements tracks = doc.select(".audio_item .ai_body");

                for (int i = 0; i != tracks.size(); i++) {
                    Element track = tracks.get(i);

                    String author = track.selectFirst(".ai_artist").text();
                    String title = track.selectFirst(".ai_title").text();

                    String encodedLink = track.selectFirst("input").val();
                    String link = linkDecoder.decode(encodedLink, client.getUid());

                    links.put(link, new String[]{author, title});
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(DDOS_DELAY);
                } catch (InterruptedException e) {
                    logger.error("DDOS DELAY: " + e.getMessage());
                }
            } while (oldLinkSize != links.size());

            return links;
        });
    }

    private interface IFetcher {
        String fetch(int page);
    }
}
