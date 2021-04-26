package com.example.boot.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author wzh
 * @since 2021/4/23
 */
public final class ReadFileUtil {
    private ReadFileUtil() {
        super();
    }

    public static String readSqlFile(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        try(InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader buffer = new BufferedReader(reader)) {
            String next;
            while((next = buffer.readLine()) != null) {
                builder.append(next);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
