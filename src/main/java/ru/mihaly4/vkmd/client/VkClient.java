package ru.mihaly4.vkmd.client;

import okhttp3.*;
import ru.mihaly4.vkmd.parse.LoginParser;

import java.io.IOException;

public class VkClient implements IVkClient {
    private static final String BASE_AUDIO_URL = "https://m.vk.com/audios";
    private static final String BASE_WALL_URL = "https://m.vk.com/";
    private static final String PING_URL = "https://m.vk.com/";
    private static final String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_0 like Mac OS X) AppleWebKit/536.26"
            + " (KHTML, like Gecko) Version/6.0 Mobile/10A5376e Safari/8536.25";

    private String remixSid = "";
    private int uid = 0;
    private OkHttpClient httpClient;

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
    public Boolean login(String username, String password) {
        // extract cookie and login url
        Request request = new Request.Builder()
                .url(PING_URL)
                .addHeader("User-Agent", USER_AGENT)
                .get()
                .build();
        Response response;
        String loginUrl = "";
        String cookie = "";
        try {
            response = getHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                loginUrl = LoginParser.parseUrl(response.body().string());
                if (response.header("Cookie") != null) {
                    cookie = response.header("Cookie");
                }
            }
        } catch (IOException | NullPointerException e) {
            return false;
        }
        if (loginUrl.isEmpty() || cookie.isEmpty()) {
            return false;
        }

        // login
        RequestBody body = new FormBody.Builder()
                .add("email", username)
                .add("pass", password)
                .build();
        request = new Request.Builder()
                .url(loginUrl)
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("Cookie", cookie)
                .post(body)
                .build();
        int uid = 0;
        String remixSid = "";
        try {
            response = getHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                uid = LoginParser.parseUid(response.body().string());
                if (response.header("Cookie") != null) {
                    remixSid = LoginParser.parseRemixSid(response.header("Cookie"));
                }
            }
        } catch (IOException | NullPointerException e) {
            return false;
        }

        if (uid == 0 || remixSid.isEmpty()) {
            return false;
        }

        this.uid = uid;
        this.remixSid = remixSid;

        return true;
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
