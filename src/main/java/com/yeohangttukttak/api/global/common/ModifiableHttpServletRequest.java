package com.yeohangttukttak.api.global.common;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ModifiableHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] modifiedBytes;

    public ModifiableHttpServletRequest(HttpServletRequest request, String modifiedData) {
        super(request);
        this.modifiedBytes = modifiedData.getBytes();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(modifiedBytes);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new RuntimeException("Not implemented.");
            }
        };
    }
}
