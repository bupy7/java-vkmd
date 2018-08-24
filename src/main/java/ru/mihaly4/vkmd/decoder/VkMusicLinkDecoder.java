package ru.mihaly4.vkmd.decoder;

import ru.mihaly4.vkmd.util.JsArrayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Fork from https://github.com/yuru-yuri/vk-audio-url-decoder-php/commit/7b4178cf6b314c09f9acb8b19619edb1439e9af8
 */
public class VkMusicLinkDecoder {
    private static final String DICT = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMN0PQRSTUVWXYZO123456789+/=";

    public String decode(String link, int uid) {
        if (!link.contains("audio_api_unavailable")) {
            return link;
        }

        String[] t = link.split("\\?extra=")[1].split("#");
        String tt = decodeR(t[0]);

        if (tt.length() == 0) {
            return link;
        }

        String n = t[1].equals("") ? "" : decodeR(t[1]);
        String[] nn = n.length() != 0 ? n.split(String.valueOf((char) 9)) : new String[0];

        int nLen = nn.length;
        while (nLen > 0) {
            --nLen;
            String[] s = nn[nLen].split(String.valueOf((char) 11));
            String[] sp = JsArrayUtil.splice(s, 0, 1, tt);

            switch (s[0].charAt(0)) {
                case 'v':
                    tt = v(sp[0]);
                    break;

                case 'r':
                    tt = r(sp[0], Integer.valueOf(sp[1]));
                    break;

                case 's':
                    tt = s(sp[0], Integer.valueOf(sp[1]));
                    break;

                case 'i':
                    tt = i(sp[0], Integer.valueOf(sp[1]), uid);
                    break;

                case 'x':
                    tt = x(sp[0], sp[1]);
                    break;

                default:
                    return link;
            }
        }

        if (tt.contains("http")) {
            return tt;
        }

        return link;
    }

    private Integer[] decodeS(String e, int t) {
        int eLen = e.length();
        Map<Integer, Integer> i = new HashMap<>();

        if (eLen > 0) {
            int o = eLen;
            t = Math.abs(t);

            while (o > 0) {
                --o;

                t = (eLen * (o + 1) ^ t + o) % eLen;
                i.put(o, t);
            }
        }

        List<Integer> keys = new ArrayList<>(i.keySet());
        Collections.sort(keys);

        Integer[] result = new Integer[keys.size()];
        for (int j = 0; j != keys.size() ; j++) {
            result[j] = i.get(j);
        }

        return result;
    }

    private String decodeR(String e) {
        if (e.equals("") || e.length() % 4 == 1) {
            return "";
        }

        int o = 0;
        int a = 0;
        int t = 0;
        String r = "";
        int eLen = e.length();

        while (a < eLen) {
            int i = DICT.indexOf(e.charAt(a));
            if (i != -1) {
                t = (o % 4 != 0) ? 64 * t + i : i;
                ++o;
                if ((o - 1) % 4 != 0) {
                    Character c = (char) (255 & t >> (-2 * o & 6));
                    if (c != '\0') {
                        r = r.concat(c.toString());
                    }
                }
            }
            ++a;
        }

        return r;
    }

    private String v(String e) {
        return new StringBuilder(e).reverse().toString();
    }

    private String r(String e, int t) {
        String[] ee = e.split("");
        String o = DICT + DICT;
        int a = ee.length;

        while (a != 0) {
            --a;
            int i = o.indexOf(ee[a]);
            if (i != -1) {
                int k = i - t;
                if (k < 0) {
                    k += o.length();
                }
                ee[a] = String.valueOf(o.charAt(k));
            }
        }

        return String.join("", ee);
    }

    private String s(String e, int t) {
        int eLen = e.length();
        String[] ee = e.split("");
        if (eLen != 0) {
            Integer[] i = decodeS(e, t);
            int o = 1;
            while (o < eLen) {
                String tmp = ee[i[eLen - 1 - o]];
                ee = JsArrayUtil.splice(ee, i[eLen - 1 - o], 1, ee[o]);
                ee[o] = tmp;
                ++o;
            }
        }
        return String.join("", ee);
    }

    private String i(String e, int t, int uid) {
        return s(e, t ^ uid);
    }

    private String x(String e, String t) {
        String data = "";
        int tt = (int) t.charAt(0);
        String[] ee = e.split("");
        for (int i = 0; i != ee.length; i++) {
            data = data.concat(String.valueOf((char) ((int) ee[i].charAt(0) ^ tt)));
        }
        return data;
    }
}
