package test;

import org.junit.jupiter.api.Test;
import src.Md2Html;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class Md2HtmlTest {
    @Test
    void test1() {
        File test1 = new File(new File(new File("test"), "md"), "test1.md");
        File out1 = new File(new File(new File("test"), "html"), "out1.html");

        File out = new File("out.md");

        Md2Html.main(new String[]{test1.getAbsolutePath(), out.getAbsolutePath()});

        Charset encoding = StandardCharsets.UTF_8;
        try (
            Reader reader_out1 = new InputStreamReader(new FileInputStream(out1), encoding);
            Reader reader_out = new BufferedReader(new InputStreamReader(new FileInputStream(out), encoding))
        ) {
            char[] buf = new char[8192];

            StringBuilder out1_data = new StringBuilder();
            while (reader_out1.read(buf) != -1) {
                out1_data.append(buf);
            }

            StringBuilder out_data = new StringBuilder();
            while (reader_out.read(buf) != -1) {
                out_data.append(buf);
            }

            assertEquals(out1_data.toString(), out_data.toString());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            fail();
        }

        assertTrue(out.delete());
    }
}