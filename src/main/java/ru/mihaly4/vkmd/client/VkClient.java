package ru.mihaly4.vkmd.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import java.io.IOException;

public class VkClient implements IVkClient {
    private static final String BASE_PROFILE_URL = "https://m.vk.com/audios";
    private static final String BASE_COMMUNITY_URL = "https://m.vk.com/";

    private String remixSid = "";
    private int uid = 0;
    private OkHttpClient httpClient;

    @Inject
    public VkClient(String remixSid, int uid) {
        this.remixSid = remixSid;
        this.uid = uid;
    }

    @Override
    public String fromProfile(int id, int offset) {
        Request request = new Request.Builder()
                .url(BASE_PROFILE_URL + id + "?offset=" + offset)
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
    public String fromCommunity(String id, int offset) {
        Request request = new Request.Builder()
                .url(BASE_COMMUNITY_URL + id + "?offset=" + offset)
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
    public String getRemixSid() {
        return remixSid;
    }

    @Override
    public int getUid() {
        return uid;
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
