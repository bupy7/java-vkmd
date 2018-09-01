package ru.mihaly4.vkmd.client;

import okhttp3.*;
import ru.mihaly4.vkmd.parser.LoginParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VkClient implements IVkClient {
    private static final String BASE_AUDIO_URL = "https://m.vk.com/audios";
    private static final String BASE_WALL_URL = "https://m.vk.com/";
    private static final String PING_URL = "https://m.vk.com/";
    private static final String COOKIE_REMIX_M_DEVICE = "375/667/1/!!-!!!!";

    private int uid = 0;
    private OkHttpClient httpClient;

    @Override
    public String fromAudio(int id, int offset) {
        Request request = new Request.Builder()
                .url(BASE_AUDIO_URL + id + "?offset=" + offset)
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
                .get()
                .build();
        Response response;
        String loginUrl = "";
        try {
            response = getHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                loginUrl = LoginParser.parseUrl(response.body().string());
            }
        } catch (IOException | NullPointerException e) {
            return false;
        }
        if (loginUrl.isEmpty()) {
            return false;
        }

        // login
        RequestBody body = new FormBody.Builder()
                .add("email", username)
                .add("pass", password)
                .build();
        request = new Request.Builder()
                .url(loginUrl)
                .post(body)
                .build();
        int uid = 0;
        String remixSid = "";
        try {
            response = getHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                uid = LoginParser.parseUid(response.body().string());
                remixSid = extractRemixSid(response.priorResponse());
            }
        } catch (IOException | NullPointerException e) {
            return false;
        }

        if (uid == 0 || remixSid.isEmpty()) {
            return false;
        }

        this.uid = uid;

        return true;
    }

    @Override
    public int getUid() {
        return uid;
    }

    private synchronized OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient()
                    .newBuilder()
                    .cookieJar(new CookieJar() {
                        private final Map<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                        @Override
                        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                            cookieStore.put(url, cookies);
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            List<Cookie> result = new ArrayList<>();

                            cookieStore.values().forEach(cookies -> cookies.forEach(cookie -> {
                                if (cookie.matches(url)) {
                                    result.add(cookie);
                                }
                            }));

                            result.add(new Cookie.Builder()
                                    .name("remixmdevice")
                                    .domain("vk.com")
                                    .value(COOKIE_REMIX_M_DEVICE)
                                    .build()
                            );

                            return result;
                        }
                    })
                    .build();
        }
        return httpClient;
    }

    private String extractRemixSid(Response response) {
        if (response != null && response.header("Set-Cookie") != null) {
            String remixSid = LoginParser.parseRemixSid(extractCookie(response.headers("Set-Cookie")));

            if (remixSid.isEmpty()) {
                return extractRemixSid(response.priorResponse());
            }

            return remixSid;
        }

        return "";
    }

    private String extractCookie(List<String> setCookies) {
        List<String> cookies = setCookies
                .stream()
                .map(mapper -> {
                    String[] result = mapper.split(";");
                    return result.length == 0 ? "" : result[0];
                })
                .collect(Collectors.toList());
        return String.join("; ", cookies);
    }
}
