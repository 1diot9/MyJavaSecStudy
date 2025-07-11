package com.idiot9.tmp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ReadDirTest {
    public static void main(String[] args) throws IOException {
        String urlContent = "";
        String read = "file:/D:\\1tmp";
        if (read != null) {
            final URL url = new URL(read);
            final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                urlContent = urlContent + inputLine + "\n";
            }
            in.close();
            System.out.println(urlContent);
        }
    }
}
