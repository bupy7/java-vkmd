package ru.mihaly4.vkmd.client;

import okhttp3.*;
import ru.mihaly4.vkmd.model.LoginResponse;
import ru.mihaly4.vkmd.parser.CaptchaParser;
import ru.mihaly4.vkmd.parser.LoginParser;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VkClient implements IVkClient {
    private static final String BASE_AUDIO_URL = "https://m.vk.com/audios";
    private static final String BASE_RELOAD_AUDIO_URL = "https://m.vk.com/audio";
    private static final String BASE_WALL_URL = "https://m.vk.com/";
    private static final String BASE_CAPTCHA_URL = "https://m.vk.com/";
    private static final String PING_URL = "https://m.vk.com/";
    private static final String COOKIE_REMIX_M_DEVICE = "375/667/1/!!-!!!!";
    private static final String USER_AGENT = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1 like Mac OS X) "
            + "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1";

    private int uid = 0;
    private OkHttpClient httpClient;
    private final Map<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

    /**
     * @param audioIds Maximum size of items is 3.
     * @return JSON
     */
    @Override
    @Nonnull
    public String reloadAudio(@Nonnull String[] audioIds) {
        FormBody.Builder requestBodyBuilder = new FormBody.Builder()
                .add("act", "reload_audio")
                .add("ids", String.join(",", audioIds));

        String json = "";

        Request request;
        try {
            request = new Request.Builder()
                    .url(BASE_RELOAD_AUDIO_URL)
                    .post(requestBodyBuilder.build())
                    .build();
        } catch (IllegalArgumentException e) {
            return json;
        }

        Response response;
        try {
            response = getHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
            }
        } catch (IOException | NullPointerException e) {
            // nothing
        }

        return json;
    }

    @Override
    @Nonnull
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
    @Nonnull
    public String fromWall(@Nonnull String id, int offset) {
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
    @Nonnull
    public LoginResponse login(@Nonnull String username, @Nonnull String password, @Nonnull String captchaCode) {
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
            return new LoginResponse(false);
        }
        if (loginUrl.isEmpty()) {
            return new LoginResponse(false);
        }

        // login
        FormBody.Builder requestBodyBuilder = new FormBody.Builder()
                .add("email", prepUsername(username))
                .add("pass", password);
        if (!captchaCode.isEmpty()) {
            requestBodyBuilder.add("captcha_key", captchaCode);
        }
        try {
            request = new Request.Builder()
                    .url(loginUrl)
                    .post(requestBodyBuilder.build())
                    .build();
        } catch (IllegalArgumentException e) {
            return new LoginResponse(false);
        }
        int uid = 0;
        String remixSid = "";
        String responseBody = "";
        try {
            response = getHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                responseBody = response.body().string();

                uid = LoginParser.parseUid(responseBody);
                remixSid = extractRemixSid(response.priorResponse());
            }
        } catch (IOException | NullPointerException e) {
            return new LoginResponse(false);
        }

        if (uid == 0 || remixSid.isEmpty()) {
            LoginResponse loginResponse = new LoginResponse(false);
            String captchaUrl = CaptchaParser.parseUrl(responseBody);
            if (!captchaUrl.isEmpty()) {
                loginResponse.setCaptcha(BASE_CAPTCHA_URL + captchaUrl.replaceFirst("^/", ""));
            } else {
                cookieStore.clear();
            }
            return loginResponse;
        }

        this.uid = uid;

        return new LoginResponse(true);
    }

    @Override
    public int getUid() {
        return uid;
    }

    private synchronized OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient()
                    .newBuilder()
                    .addNetworkInterceptor(chain -> {
                        Request originalRequest = chain.request();
                        Request requestWithUserAgent = originalRequest.newBuilder()
                                .header("User-Agent", USER_AGENT)
                                .build();
                        return chain.proceed(requestWithUserAgent);
                    })
                    .cookieJar(new CookieJar() {
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

    @Nonnull
    private String prepUsername(@Nonnull String username) {
        if (username.contains("@")) {
            return username;
        }
        return username.replaceAll("[^\\d]+", "");
    }
}
