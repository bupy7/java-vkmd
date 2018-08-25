package ru.mihaly4.vkmd.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.mihaly4.vkmd.model.Credential;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class VkClient implements IVkClient {
    private static final String BASE_AUDIO_URL = "https://m.vk.com/audios";
    private static final String BASE_WALL_URL = "https://m.vk.com/";

    private String remixSid = "";
    private OkHttpClient httpClient;

    @Inject
    public VkClient(String remixSid) {
        this.remixSid = remixSid;
    }

    @Override
    public String fromAudio(int id, int offset) {
        Request request = new Request.Builder()
                .url(BASE_AUDIO_URL + id + "?offset=" + offset)
                .addHeader("Cookie", "remixsid=" + remixSid)
                .get()
                .build();
        Response response;
        String html = "";
        try {
            response = getHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                html = response.body().string();
            }
        } catch (IOException | NullPointerException e) {
            // nothing
        }

        return html;
    }

    @Override
    public String fromWall(String id, int offset) {
        Request request = new Request.Builder()
                .url(BASE_WALL_URL + id + "?offset=" + offset)
                .addHeader("Cookie", "remixsid=" + remixSid + ";remixmdevice=375/667/1/!!-!!!!")
                .get()
                .build();
        Response response;
        String html = "";
        try {
            response = getHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                html = response.body().string();
            }
        } catch (IOException | NullPointerException e) {
            // nothing
        }

        return html;
    }

    @Override
    public CompletableFuture<Credential> login(String username, String password) {
        return CompletableFuture.supplyAsync(() -> {
            // @TODO
            return new Credential();
        });
    }

    private synchronized OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient()
                    .newBuilder()
                    .followRedirects(false)
                    .followSslRedirects(false)
                    .build();
        }
        return httpClient;
    }
}
