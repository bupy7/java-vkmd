package ru.mihaly4.vkmd.parser;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaptchaParser {
    @Nonnull
    public static String parseUrl(@Nonnull String data) {
        Pattern p = Pattern.compile("src=\"(/captcha.php[^\"]+)\"");

        Matcher matches = p.matcher(data);

        return matches.find() ? matches.group(1) : "";
    }
}
