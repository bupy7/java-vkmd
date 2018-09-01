package ru.mihaly4.vkmd.parser;

import org.junit.Test;
import ru.mihaly4.vkmd.model.Target;

import static org.junit.Assert.*;

public class TargetParserTest {
    @Test
    public void parseTarget() {
        String url = "https://vk.com/feed";
        Target target = TargetParser.parseTarget(url);
        assertNotNull(target);
        assertEquals("feed", target.getValue());
        assertEquals(20, target.getType());

        url = "https://vk.com/id1";
        target = TargetParser.parseTarget(url);
        assertNotNull(target);
        assertEquals("id1", target.getValue());
        assertEquals(20, target.getType());

        url = "https://vk.com/audios876";
        target = TargetParser.parseTarget(url);
        assertNotNull(target);
        assertEquals("876", target.getValue());
        assertEquals(10, target.getType());

        url = "https://vk.com/audios-876";
        target = TargetParser.parseTarget(url);
        assertNotNull(target);
        assertEquals("-876", target.getValue());
        assertEquals(10, target.getType());

        url = "https://vk.ru/invalid";
        target = TargetParser.parseTarget(url);
        assertNull(target);
    }
}
