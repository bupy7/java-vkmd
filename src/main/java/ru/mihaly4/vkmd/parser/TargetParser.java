package ru.mihaly4.vkmd.parser;

import org.jetbrains.annotations.Nullable;
import ru.mihaly4.vkmd.model.Target;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TargetParser {
    @Nullable
    public static Target parseTarget(String url) {
        Pattern p = Pattern.compile("com/audios((-|)[\\d]+)");
        Matcher matches = p.matcher(url);
        if (matches.find()) {
            Target target = new Target(matches.group(1));
            target.setType(Target.TYPE_AUDIO);
            return target;
        }

        p = Pattern.compile("com/(.+)");
        matches = p.matcher(url);
        if (matches.find()) {
            Target target = new Target(matches.group(1));
            target.setType(Target.TYPE_WALL);
            return target;
        }

        return null;
    }
}
