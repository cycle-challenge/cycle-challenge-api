package com.yeohangttukttak.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleRevokeService {

    private static final RestClient client = RestClient.create();

    public void call(String refreshToken) {

        client.post()
                .uri(URI.create("https://oauth2.googleapis.com/revoke?token=" + refreshToken))
                .retrieve()
                .toBodilessEntity();
    }

}
