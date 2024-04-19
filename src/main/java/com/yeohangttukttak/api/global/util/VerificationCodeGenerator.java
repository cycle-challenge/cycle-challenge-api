package com.yeohangttukttak.api.global.util;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class VerificationCodeGenerator {

    public String strong() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstanceStrong();

        int code = random.nextInt(1000000);
        return String.format("%06d", code);
    }

}
