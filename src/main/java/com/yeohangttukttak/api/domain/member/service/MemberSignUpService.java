package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.AuthType;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.member.entity.Password;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberSignUpService {

    private final MemberRepository memberRepository;

    public void local(String email, String password, String nickname) {

        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new ApiException(ApiErrorCode.DUPLICATED_EMAIL);
                });

        memberRepository.findByNickname(nickname)
                .ifPresent(member -> {
                    throw new ApiException(ApiErrorCode.DUPLICATED_NICKNAME);
                });

        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .password(Password.create(password))
                .authType(AuthType.LOCAL)
                .build();

        memberRepository.save(member);

    }

}
