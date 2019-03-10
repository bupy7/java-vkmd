package ru.mihaly4.vkmd.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaptchaParserTest {
    @Test
    public void parseUrl() {
        String data = "<img id=\"captcha\" alt=\"Picture with code\" src=\"/captcha.php?s=0&sid=11234567\" class=\"captcha_img\"/>";
        String url = CaptchaParser.parseUrl(data);

        assertEquals(
                "/captcha.php?s=0&sid=11234567",
                url
        );

        // second option
        data = "<img id=\"captcha\" alt=\"Picture with code\" src=\"/img1.php?time=12344\" class=\"captcha_img\"/>";
        url = CaptchaParser.parseUrl(data);

        assertEquals("", url);
    }
}
