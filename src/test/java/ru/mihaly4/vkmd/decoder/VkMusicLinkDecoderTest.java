package ru.mihaly4.vkmd.decoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import org.junit.Test;

import static org.junit.Assert.*;

public class VkMusicLinkDecoderTest {
    @Test
    public void decode() {
        String link = "https://m.vk.com/mp3/audio_api_unavailable.mp3?extra=Afjjz1LKsg9lmZjwoxvZyZbKBxHOq1u/uwvuyKrXEuv"
            + "Hztu5DJvwouzFCej2C1nLD29mCMKOt3qOwveUzc5oBgf5A3D4ndqZqOLrlJLUmZq3tI11sgK3AgrHEdLdt2vNnNnJt21dAOrKDvr1BJy"
            + "Tlw0XDc10rvL3Bt1PstzOywTWnc9TDgmZqNu5tOCOmMDotIOZCheTn1j5u1H2nhiVCsOOmI9loJCVrf8VnvmYmxmYD3Hhrw8Xn1eYnhn"
            + "PqZiOv2fSswfSndbWEvzfDgnzn2rvmMnYneLOBZi5x1K3u1D4swTWos9yvq#AqS4mdC";
        String expected = "https://psv4.vkuseraudio.net/c422724/u40724969/audios/4a24493736c9.mp3?extra=EiC9YYm5Ch4cq4S"
            + "35XaaK7QG9t-mLgIH-CtwcsWvQy9BFDlkDX4hxS_llVdOxExg77IUpD2q_U1-5w2EwWNTYHp_Q1Uiru0V2n-q1yEK439oId-cI-TVkGj"
            + "BONY4by-SgtmORIwdQkYy6ONSeo2R7xCdCNI";
        assertEquals(expected, new VkMusicLinkDecoder().decode(link, 520969309));

        link = "https://m.vk.com/mp3/audio_api_unavailable.mp3?extra=Af93q2zittm4DgLOBhzQq3nWmhzHCOW6C3HWEgLJvKfNn3uTCZ"
            + "1TngXbzveYEMjJCJbvne15DdHwCZbuDLjLvtfOrJuVmLPjugT4Au1vzxaXnuPNmK9wtZHJzdrwlNf0AtzIqunbx21rrg1vEJDwzLfwBg"
            + "i3qtvHyt9NogSZAJv5mtDQm3jVDZbLwveVowmWvI9yz29Jys1Zm2TMzZnuy3fbzdrLnLHnD3yOCe5LzhbHCs90Dhy4Dw0TCJjLz3LrsO"
            + "DwzvbpBM9HvwqUBOfyBZbpvdL1lKv2nvy#AqS5nJG";
        expected = "https://cs1-80v4.vkuseraudio.net/p19/b077572e87826e.mp3?extra=oOjMUV0Q8mlfV8vp2csQ_iyjjVzlvPwXE4Agi"
            + "acVjC5LTlo4-d5oNQkku4PTUA5arxd3gUzCw4qAADcvOx0XH_e93bf1t-vfgAGYA3s0hUgUcsqo2KaOFehCbVypQVdMwZOgVTqRM6mIg"
            + "M5Jy3XVVQ0aeiAc";
        assertEquals(expected, new VkMusicLinkDecoder().decode(link, 520969309));
    }

    @Test
    public void decodeIAndSMode() {
        String link = "https://m.vk.com/mp3/audio_api_unavailable.mp3?extra=AdzfDufpDwCTmvjLowf2oxPZmMnJqxu6l1jLB3qOBJL"
            + "JvwmZzdmVnMO4meTZmen5ntGOEgC5mvnwwM9imdiOuLDmngLkuvPTDf90sMfYvePXAOzAx1rLp2DUyxzQCZGUohLOus9Ux2XSrMzooc5"
            + "ZBgfKsNbPvtq3D2fItJqZtfHIAJrnnwHLALi1mNmZmZrpBf9byKGVvJrrAgPOlZDnnvC2EgnLwMziBKDhthHWpwLrrLf4EgfSB2eVogm"
            + "ZnuLNztfRntLhCdbTqMvRCML1nhHIlNPjD1jsAxrZBhi#AqSOnZG";
        String expected = "https://psv4.vkuseraudio.net/c813331/u444529088/audios/f44c7eb9100a.mp3?extra=iOl2yV9At8hlUA"
            + "qI3sOcR5-46m_JHNS0bLNMFHR7za6LnHRTRZzmRl9Zcge5GxUy4FjsXQroTZxWwkJcbjnebjk3lxE54ch89xaGI2ga8QV6ZiGiJlMQAl"
            + "LW5fRB4_JgKQe5jgnxw__hCFQ";
        assertEquals(expected, new VkMusicLinkDecoder().decode(link, 444529088));

        link = "https://m.vk.com/mp3/audio_api_unavailable.mp3?extra=AdrvDMPnD3vHC2C2nMeOEdbsvKfnDvfOrMjfDJHLuMrqAwuZDZ"
            + "iVm2e4yZjZmOq5vw85ysO5mLjLEgntmdiOAMfNm2LZmxjTDgjpzhHYzxPSAOfKCMrLp3PUDJG4wtGUweHOws82BhL0r2XFoc5qvhHKDh"
            + "vYzdrrDMq2EsO2nJHlDNHkwK9LDNy1shmWm2f0DuLuoe0VyLm6DeHOlZvkmK9WEgjwBgf3DuvkCJDWpwLRB30XD2fTB2KVohnPvLjLyJ"
            + "DczKXyCc9bBKPxr291neKUsheTCvzdzNmXodi#AqS1nJe";
        expected = "https://psv4.vkuseraudio.net/c813622/u444529088/audios/aa3e5b6d73c8.mp3?extra=rJuG9eH60XOqdTl-iPhPa"
            + "2t-vaVsS_wi6gyJQwyvVeb6uJgdjdqAvTRlszOfX14H9GvY812QexwOvWUF8H6xKxME8IC8Jbt8LUxBRHRxokb3rfEodmIzwhrAZlVnS"
            + "jza2YJV8-R7bli0DAM";
       assertEquals(expected, new VkMusicLinkDecoder().decode(link, 444529088));
    }

    @Test
    public void decodeRMode() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        VkMusicLinkDecoder vkMusicLinkDecoder = new VkMusicLinkDecoder();
        Method method = vkMusicLinkDecoder.getClass().getDeclaredMethod("r", String.class, int.class);
        method.setAccessible(true);
        assertEquals("Y++69:PP6R9+VSZ4.T53P", method.invoke(vkMusicLinkDecoder, "https://pastebin.com/", 22));
    }

    @Test
    public void decodeVMode() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        VkMusicLinkDecoder vkMusicLinkDecoder = new VkMusicLinkDecoder();
        Method method = vkMusicLinkDecoder.getClass().getDeclaredMethod("v", String.class);
        method.setAccessible(true);
        assertEquals("abc", method.invoke(vkMusicLinkDecoder, "cba"));
    }

    @Test
    public void decodeXMode() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        VkMusicLinkDecoder vkMusicLinkDecoder = new VkMusicLinkDecoder();
        Method method = vkMusicLinkDecoder.getClass().getDeclaredMethod("x", String.class, String.class);
        method.setAccessible(true);
        String expected = "WkZGQkEIHR1CU0FGV1BbXBxRXV8d";
        assertEquals(
                new String(Base64.getDecoder().decode(expected)),
                method.invoke(vkMusicLinkDecoder, "https://pastebin.com/", "22")
        );
    }
}
