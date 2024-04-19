package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.EmailVerificationCodeRepository;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dto.MemberDTO;
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

    private final EmailVerificationCodeRepository verificationCodeRepository;

    public MemberDTO local(String email, String password, String nickname, String verificationCode) {

        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new ApiException(ApiErrorCode.DUPLICATED_EMAIL);
                });

        memberRepository.findByNickname(nickname)
                .ifPresent(member -> {
                    throw new ApiException(ApiErrorCode.DUPLICATED_NICKNAME);
                });

        String existCode = verificationCodeRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ApiErrorCode.INVALID_VERIFICATION_CODE));

        if (!existCode.equals(verificationCode)) {
            throw new ApiException(ApiErrorCode.INVALID_VERIFICATION_CODE);
        }

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
