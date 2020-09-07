package ru.mihaly4.vkmd.parser;

import org.json.JSONArray;
import org.json.JSONException;

import javax.annotation.Nonnull;

public class AudioParser {
    @Nonnull
    public static String compileAudioId(@Nonnull String audioDataJson) {
        try {
            JSONArray audioData = new JSONArray(audioDataJson);

            String fullId = audioData.get(1) + "_" + audioData.get(0);
            String[] tokens = ((String)audioData.get(13)).split("/");
            String actionHash = tokens.length > 2 ? tokens[2] : "";
            String urlHash = tokens.length > 5 ? tokens[5] : "";

            return fullId + "_" + actionHash + "_" + urlHash;
        } catch (JSONException e) {
            return "";
        }
    }
}
