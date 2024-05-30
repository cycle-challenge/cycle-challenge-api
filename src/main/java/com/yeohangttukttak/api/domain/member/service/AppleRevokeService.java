package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
@Transactional
public class AppleRevokeService {

    private final String CLIENT_SECRET;

    public AppleRevokeService(@Value("${APPLE_CLIENT_SECRET}") String CLIENT_SECRET) {
        this.CLIENT_SECRET = CLIENT_SECRET;
    }

    private static final RestClient client = RestClient.create();

    public void call(String refreshToken) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("client_id", "app.yeohaeng.ttukttak.com");
        body.add("client_secret", CLIENT_SECRET);
        body.add("token", refreshToken);

        client.post()
                .uri(URI.create("https://appleid.apple.com/auth/revoke"))
                .contentType(APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toBodilessEntity();

    }

}
