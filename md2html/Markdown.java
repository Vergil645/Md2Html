package md2html;

import java.util.AbstractMap;
import java.util.Map;

public class Markdown {
    public static final int maxTagLength;

    public static final Map<String, String> TAGS = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("**", "strong"),
            new AbstractMap.SimpleEntry<>("__", "strong"),
            new AbstractMap.SimpleEntry<>("--", "s"),
            new AbstractMap.SimpleEntry<>("`", "code"),
            new AbstractMap.SimpleEntry<>("*", "em"),
            new AbstractMap.SimpleEntry<>("_", "em"),
            new AbstractMap.SimpleEntry<>("~", "mark")
    );

    public static final Map<String, String> SPECIAL_SYMBOLS = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("<", "&lt;"),
            new AbstractMap.SimpleEntry<>(">", "&gt;"),
            new AbstractMap.SimpleEntry<>("&", "&amp;")
    );

    static {
        int max = 0;
        for (String str : TAGS.keySet()) {
            max = Math.max(max, str.length());
        }
        maxTagLength = max;
    }
}
