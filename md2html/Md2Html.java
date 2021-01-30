package md2html;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class Md2Html {
    public static void main(String[] args) {
        Charset encoding = StandardCharsets.UTF_8;
        try (
            Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), encoding));
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), encoding))
        ) {
            new Converter(in).convert(out);
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    private static class Converter extends BaseConverter {
        private static final String LINE_SEPARATOR = System.lineSeparator();
        private static final String LINE_SEPARATOR_SEPARATOR = LINE_SEPARATOR.repeat(2);
        private static final String LINE_SEPARATOR_END = System.lineSeparator() + END;
        private final Set<Integer> links;

        public Converter(Reader reader) throws IOException {
            super(FileSource.readAll(reader));
            links = new HashSet<>();
        }

        private void readLinks() {
            links.clear();
            int beginPos = getPos();
            int linkPos = -1;
            int phase = 0;
            while (!eof() && !test(LINE_SEPARATOR_SEPARATOR) && !test(LINE_SEPARATOR_END)) {
                if (phase == 0 && check('[')) {
                    linkPos = getPos();
                    ++phase;
                } else if (phase == 1 && check("](")) {
                    ++phase;
                } else if (phase == 2 && check(')')) {
                    links.add(linkPos);
                    phase = 0;
                } else {
                    skip(1);
                }
            }
            jmp(beginPos);
        }

        public void convert(final Writer out) throws IOException {
            skipLineSeparators();
            while (!eof()) {
                int cnt = 0;
                readLinks();
                while (test('#')) {
                    ++cnt;
                }
                if (0 < cnt && cnt <= 6 && test(' ')) {
                    out.write("<h" + cnt + ">" + block(new HashSet<>()) + "</h" + cnt + ">\n");
                } else {
                    out.write("<p>" + "#".repeat(cnt) + block(new HashSet<>()) + "</p>\n");
                }
                skipLineSeparators();
            }
        }

        private String block(final Set<String> tags) {
            StringBuilder sb = new StringBuilder();
            outer : while (!eof()) {
                if (check(LINE_SEPARATOR_SEPARATOR) || check(LINE_SEPARATOR_END)) {
                    return sb.toString();
                } else if (links.contains(getPos())) {
                    convertLink(sb);
                } else if (test('\\')) {
                    sb.append(nextChar());
                } else if (Markdown.SPECIAL_SYMBOLS.containsKey(substring(1))) {
                    sb.append(Markdown.SPECIAL_SYMBOLS.get(substring(1)));
                    nextChar();
                } else {
                    for (int len = 1; len <= Markdown.maxTagLength; len++) {
                        if (tags.contains(substring(len))) {
                            return sb.toString();
                        }
                    }
                    for (int len = Markdown.maxTagLength; len > 0; len--) {
                        if (Markdown.TAGS.containsKey(substring(len))) {
                            convertMarkup(tags, sb, substring(len));
                            continue outer;
                        }
                    }
                    sb.append(nextChar());
                }
            }
            return sb.toString();
        }

        private void convertLink(final StringBuilder sb) {
            skip(1);
            String str = block(new HashSet<>(Set.of("](")));
            skip(2);
            StringBuilder link = new StringBuilder();
            while (!test(')')) {
                link.append(nextChar());
            }
            sb.append("<a href='").append(link).append("'>").append(str).append("</a>");
        }

        private void convertMarkup(Set<String> tags, StringBuilder sb, String md) {
            tags.add(md);
            skip(md.length());
            String str = block(tags);
            tags.remove(md);
            boolean condition = !check(md);
            for (int len = md.length() + 1; len <= Markdown.maxTagLength; len++) {
                if (tags.contains(substring(len))) {
                    condition = true;
                    break;
                }
            }
            if (condition) {
                sb.append(md).append(str);
            } else {
                sb.append("<").append(Markdown.TAGS.get(md)).append(">");
                sb.append(str);
                sb.append("</").append(Markdown.TAGS.get(md)).append(">");
                skip(md.length());
            }
        }

        private void skipLineSeparators() {
            while (test(LINE_SEPARATOR));
        }
    }
}
