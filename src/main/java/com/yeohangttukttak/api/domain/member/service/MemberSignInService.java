package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.dto.MemberAuthDTO;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.member.entity.RefreshToken;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSignInService {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public MemberAuthDTO local(String email, String password) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ApiErrorCode.SIGN_IN_FAILED));

        if (!member.getPassword().validate(password))
            throw new ApiException(ApiErrorCode.SIGN_IN_FAILED);

        Instant now = Instant.now();
        Long refreshTokenTTL = 3600 * 24 * 14L;

        String accessToken = tokenService.issue(email, now, 1800L);
        String refreshToken = tokenService.issue(email, now, refreshTokenTTL);

        refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken, refreshTokenTTL));

        return new MemberAuthDTO(accessToken, refreshToken);

    }

}
