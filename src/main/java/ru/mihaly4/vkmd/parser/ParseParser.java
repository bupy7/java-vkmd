package ru.mihaly4.vkmd.parser;

import org.jetbrains.annotations.Nullable;
import ru.mihaly4.vkmd.model.Target;

public class ParseParser {
    @Nullable
    public static Target parseTarget(String url) {
        return new Target(url);
    }
}
