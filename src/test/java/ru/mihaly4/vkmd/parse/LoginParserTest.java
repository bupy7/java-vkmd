package ru.mihaly4.vkmd.parse;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class LoginParserTest {
    @Test
    public void parseUrl() {
        String data = "<form method=\"post\" action=\"https://login.vk.com/?act=login&_origin=https://m.vk.com&ip_h=5d5dc55da26f0b5bae&lg_h=c8e2d8eaa3af30c829&role=pda&utf8=1\" novalidate=\"\">";
        String url = LoginParser.parseUrl(data);

        assertEquals(
                "https://login.vk.com/?act=login&_origin=https://m.vk.com&ip_h=5d5dc55da26f0b5bae&lg_h=c8e2d8eaa3af30c829&role=pda&utf8=1",
                url
        );

        // second option
        data = "<form method=\"post\" novalidate=\"\">";
        url = LoginParser.parseUrl(data);

        assertEquals("", url);
    }

    @Test
    public void parseUid() {
        URL fixture = getClass().getResource("/fixture/profile.html");

        String data = "";
        try {
            data = Resources.toString(fixture, Charsets.UTF_8);
        } catch (IOException e) {
            // nothing
        }

        int uid = LoginParser.parseUid(data);

        assertEquals(444529088, uid);

        // second option
        uid = LoginParser.parseUid("class=\"ip_user_link\"");

        assertEquals(0, uid);
    }

    @Test
    public void parseRemixSid() {
        String[] cookie1 = {
            "remixlang=0",
            "remixlhk=4c876f03571527d1e2",
            "remixsid=af9454bba9f3a0c35f370cb6af6e862b41d25ce1f713fxyu7ef7b",
            "remixstid=0_a4exb2ebb6b918adc5"
        };

        String remixSid = LoginParser.parseRemixSid(String.join("; ", cookie1));

        assertEquals("af9454bba9f3a0c35f370cb6af6e862b41d25ce1f713fxyu7ef7b", remixSid);

        // second option
        String[] cookie2 = {
                "remixlang=0",
                "remixlhk=4c876f02591522d1e2",
                "remixstid=0_a4sbb2ebb6b918adc5"
        };

        remixSid = LoginParser.parseRemixSid(String.join("; ", cookie2));

        assertEquals("", remixSid);
    }
}
