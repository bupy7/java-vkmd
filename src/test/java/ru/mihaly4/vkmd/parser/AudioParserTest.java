package ru.mihaly4.vkmd.parser;

import org.json.JSONArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AudioParserTest {
    @Test
    public void compileIdsToReloadAudio() {
        String audioDataJson = "[456239180,520969309,\"\",\"&quot;Сотрудник банка&quot; с табельным номером\",\"Александр\",121,0,0,\"\",0,2,\"my:my_audios\",\"[]\",\"e3f5c888f81141e614\\/c393a969ec445ea9e3\\/8479df88e3ca4a26be\\/8fd54cbf0be3f60f93\\/\\/324bd8a29b879da5a6\\/3981b41c6de1a0925f\",\"\",{\"duration\":121,\"content_id\":\"520969309_456239180\",\"puid22\":11,\"account_age_type\":2,\"_SITEID\":276,\"vk_id\":520969309,\"ver\":251116},\"\",\"\",\"\",false,\"9d00c5212bkZhFVA1dbjzwqwWpZydpzWYYSKqkwHtam--W8_qfiN6zWrOEzjsr7PDqA3n3J2nJ9l454rCyXXDwub_TTROvnqv-M7g4Q-az48oOiQmGdxCMjmPihqJIZPCI1JF6nvr7vFJI3hgIM\",0,0,true,\"\",false]";
        JSONArray audioData = new JSONArray(audioDataJson);

        String fullId = audioData.get(1) + "_" + audioData.get(0);
        String[] tokens = ((String)audioData.get(13)).split("/");
        String actionHash = tokens.length > 2 ? tokens[2] : "";
        String urlHash = tokens.length > 5 ? tokens[5] : "";
        String audioReloadId = fullId + "_" + actionHash + "_" + urlHash;

        assertEquals("520969309_456239180_8479df88e3ca4a26be_324bd8a29b879da5a6", audioReloadId);
    }
}
