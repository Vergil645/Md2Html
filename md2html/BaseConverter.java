package md2html;

public abstract class BaseConverter {
    protected static final char END = '\0';
    private final String data;
    private int pos;

    protected BaseConverter(final String data) {
        this.data = data;
        this.pos = 0;
    }

    protected char getChar() {
        return pos < data.length() ? data.charAt(pos) : END;
    }

    protected int getPos() {
        return pos;
    }

    protected char nextChar() {
        char tmp = getChar();
        ++pos;
        return tmp;
    }

    protected boolean eof() {
        return getChar() == END;
    }

    protected String substring(final int len) {
        if (pos >= data.length()) {
            return String.valueOf(END);
        }
        return pos + len > data.length() ?
                data.substring(pos) + String.valueOf(END).repeat(pos + len - data.length()) :
                data.substring(pos, pos + len);
    }

    protected void skip(final int count) {
        pos += count;
    }

    protected void jmp(final int newPos) {
        pos = newPos;
    }

    protected boolean check(final char expected) {
        return getChar() == expected;
    }

    protected boolean check(final String expected) {
        return substring(expected.length()).equals(expected);
    }

    protected boolean test(final char expected) {
        if (check(expected)) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean test(final String expected) {
        if (check(expected)) {
            skip(expected.length());
            return true;
        }
        return false;
    }
}
