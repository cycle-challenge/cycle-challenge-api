package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.dto.MemberAuthDTO;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MemberAuthRenewService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public MemberAuthDTO renew (String refreshToken, String email) {
        // 1. 토큰의 Email로 Member ID(Seq)를 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ApiErrorCode.INVALID_AUTHORIZATION));

        // 2. 서버 저장소에 Refresh Token이 존재하는지 확인
        String existToken = refreshTokenRepository.findById(member.getId())
                .orElseThrow(() -> new ApiException(ApiErrorCode.INVALID_AUTHORIZATION));

        // 3. 저장된 Refresh Token이 유효한지 확인
        if (!existToken.equals(refreshToken))
            throw new ApiException(ApiErrorCode.INVALID_AUTHORIZATION);

        Instant now = Instant.now();

        JwtToken newAccessToken = JwtToken.issueAccessToken(email, now);
        JwtToken newRefreshToken = JwtToken.issueRefreshToken(email, now);

        refreshTokenRepository.save(member.getId(), newRefreshToken.getToken(), JwtToken.refreshTokenTTL);

        return new MemberAuthDTO(newAccessToken, newRefreshToken);
    }

}
