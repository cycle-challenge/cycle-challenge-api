package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dto.MemberDTO;
import com.yeohangttukttak.api.domain.member.entity.AgeGroup;
import com.yeohangttukttak.api.domain.member.entity.AuthType;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.member.entity.Password;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberSignUpService {

    private final MemberRepository memberRepository;

    public MemberDTO local(String email, String password, String nickname) {

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

        Long memberID = memberRepository.save(member);

        Member createdMember = memberRepository.find(memberID)
                .orElseThrow(() -> new ApiException(ApiErrorCode.INTERNAL_SERVER_ERROR));

        return new MemberDTO(createdMember);

    }

}
