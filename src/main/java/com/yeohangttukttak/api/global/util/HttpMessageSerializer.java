package com.yeohangttukttak.api.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpMessageSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void serialize(HttpServletResponse httpResponse,
                                 int status, Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            httpResponse.setStatus(status);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
