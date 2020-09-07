package ru.mihaly4.vkmd.repository;

import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.mihaly4.vkmd.client.IVkClient;
import ru.mihaly4.vkmd.decoder.VkMusicLinkDecoder;
import ru.mihaly4.vkmd.log.ILogger;
import ru.mihaly4.vkmd.parser.AudioParser;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class VkRepository {
    private static final int AUDIO_LIMIT = 50;
    private static final int WALL_LIMIT = 5;
    private static final long DDOS_DELAY = 1000;
    private static final int AUDIO_IDS_CHUNK_SIZE = 3;

    private IVkClient client;
    private VkMusicLinkDecoder linkDecoder;
    private ILogger logger;

    public VkRepository(IVkClient client, VkMusicLinkDecoder linkDecoder, ILogger logger) {
        this.client = client;
        this.linkDecoder = linkDecoder;
        this.logger = logger;
    }

    public CompletableFuture<Map<String, String[]>> findAllByWall(String id) {
        return findAll(page -> client.fromWall(id, WALL_LIMIT * page), ".wi_body .audio_item");
    }

    public CompletableFuture<Map<String, String[]>> findAllByAudio(int id) {
        return findAll(page -> client.fromAudio(id, AUDIO_LIMIT * page), ".AudioPlaylistRoot .audio_item");
    }

    private CompletableFuture<Map<String, String[]>> findAll(IFetcher fetcher, String selector) {
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, String[]> links = new HashMap<>();
            int oldLinkSize = 0;
            int page = 0;

            do {
                oldLinkSize = links.size();

                Document doc = Jsoup.parse(fetcher.fetch(page++));

                Elements tracks = doc.select(selector);

                final ArrayList<String> audioIds = new ArrayList<>();

                for (int i = 0; i != tracks.size(); i++) {
                    Element track = tracks.get(i);
                    String audioData = track.dataset().getOrDefault("audio", "[]");

                    String audioId = AudioParser.compileAudioId(audioData);

                    if (!audioId.isEmpty()) {
                        audioIds.add(audioId);
                    }
                }

                List<List<String>> partedAudioIds = Lists.partition(audioIds, AUDIO_IDS_CHUNK_SIZE);

                for (int j = 0; j != partedAudioIds.size(); j++) {
                    String reloadAudioResponse = client.reloadAudio(partedAudioIds.get(j).toArray(new String[]{}));

                    try {
                        JSONObject reloadAudioData = new JSONObject(reloadAudioResponse);
                        JSONArray audioUrls = reloadAudioData.getJSONArray("data").getJSONArray(0);
                        for (int i = 0; i != audioUrls.length(); i++) {
                            JSONArray audioUrl = audioUrls.optJSONArray(i);

                            String author = audioUrl.optString(4);
                            String title = audioUrl.optString(3);
                            String link = audioUrl.optString(2);

                            // decode api_unavailable
                            link = linkDecoder.decode(link, client.getUid());
                            // decode m3u8
                            link = linkDecoder.decode(link, client.getUid());

                            links.put(link, new String[]{author, title});
                        }
                    } catch (JSONException e) {
                        logger.error("INVALID JSON: " + e.getMessage());
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(DDOS_DELAY);
                    } catch (InterruptedException e) {
                        logger.error("DDOS DELAY: " + e.getMessage());
                    }
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
