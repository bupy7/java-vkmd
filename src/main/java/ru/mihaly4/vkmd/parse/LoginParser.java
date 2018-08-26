package ru.mihaly4.vkmd.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginParser {
    public static String parseUrl(String data) {
        Pattern p = Pattern.compile("action=\"([^\"]+)");

        Matcher matches = p.matcher(data);

        return matches.find() ? matches.group(1) : "";
    }

    public static int parseUid(String data) {
        Pattern p = Pattern.compile("class=\"ip_user_link[\\s\\S]+href=\"/id([\\d]+)");

        Matcher matches = p.matcher(data);

        return matches.find() ? Integer.valueOf(matches.group(1)) : 0;
    }

    public static String parseRemixSid(String data) {
        Pattern p = Pattern.compile("remixsid=([^;]+)");

        Matcher matches = p.matcher(data);

        return matches.find() ? matches.group(1) : "";
    }
}
