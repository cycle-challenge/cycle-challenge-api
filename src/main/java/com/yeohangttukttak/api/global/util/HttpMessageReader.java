package com.yeohangttukttak.api.global.util;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpMessageReader {

    public static String readBody(HttpServletRequest httpRequest) throws IOException {
        InputStream inputStream = httpRequest.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        try {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)  {
                sb.append(line);
            }

            return sb.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
