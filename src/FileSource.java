package src;

import java.io.*;

public class FileSource {
    public static String readAll(final Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        int read = reader.read();
        while (read != -1) {
            sb.append((char) read);
            read = reader.read();
        }
        return sb.toString();
    }
}
