package ru.mihaly4.vkmd.parser;

import ru.mihaly4.vkmd.model.Target;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TargetParser {
    @Nullable
    public static Target parseTarget(@Nonnull String url) {
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
