package com.nicholaszhou.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class IOUtils {
    public static String inputStreamToString(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            return "";
        }

        StringBuilder responseStrBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseStrBuilder.append(line).append("\n");
            }

        }

        return responseStrBuilder.toString();
    }

    public static String toString(byte[] input, String charsetName) throws IOException {
        return new String(input, Charsets.toCharset(charsetName));
    }
}
