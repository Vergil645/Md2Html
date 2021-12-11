package src;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Md2Html {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("ERROR: Wrong number of arguments\n" +
                               "       Expected 2 arguments: <input filename> <output filename>\n" +
                               "       Found: " + args.length);
            return;
        }

        Charset encoding = StandardCharsets.UTF_8;
        try (
            Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), encoding));
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), encoding))
        ) {
            new Converter(in).convert(out);
            System.out.println("Converted successfully!");
        } catch (IOException e) {
            System.err.format("ERROR: %s\n", e.getMessage());
        }
    }

}
