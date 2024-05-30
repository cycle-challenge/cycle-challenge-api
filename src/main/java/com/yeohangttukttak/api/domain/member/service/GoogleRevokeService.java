package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleRevokeService {

    private final MemberRepository memberRepository;

    private static final RestClient client = RestClient.create();

    public void call(Member member) {

        client.post()
                .uri(URI.create("https://oauth2.googleapis.com/revoke?token=" + member.getRefreshToken()))
                .retrieve()
                .toBodilessEntity();

        memberRepository.delete(member);

    }

}
