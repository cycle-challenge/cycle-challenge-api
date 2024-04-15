package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.member.entity.RefreshToken;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MemberAuthRenewServiceTest {

    @InjectMocks
    private MemberAuthRenewService authRenewService;

    @Mock
    private TokenService tokenService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;


    @Test
    public void 정상_케이스() throws Exception {
        // given
        String email = "test@example.com";
        String refreshToken = "test.refresh.token";

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .build();

        RefreshToken existToken = new RefreshToken(member.getId(), refreshToken,1L);

        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));

        given(refreshTokenRepository.findById(member.getId()))
                .willReturn(Optional.of(existToken));

        // when
        authRenewService.renew(refreshToken, email);

        // then
        then(refreshTokenRepository).should(never()).findById(member.getId());
        then(refreshTokenRepository).should(times(1)).save(any());
    }

    @Test
    public void 사용자_존재하지_않음() throws Exception {
        // given
        String email = "test@example.com";
        String refreshToken = "test.refresh.token";

        given(memberRepository.findByEmail(email))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> authRenewService.renew(refreshToken, email))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ApiErrorCode.INVALIDED_AUTHORIZATION.name());

        then(refreshTokenRepository).should(never()).findById(any());
        then(refreshTokenRepository).should(never()).save(any());
    }


    @Test
    public void 토큰_존재하지_않음() throws Exception {
        // given
        String email = "test@example.com";
        String refreshToken = "test.refresh.token";

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .build();

        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));

        given(refreshTokenRepository.findById(member.getId()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> authRenewService.renew(refreshToken, email))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ApiErrorCode.INVALIDED_AUTHORIZATION.name());

        then(refreshTokenRepository).should(times(1)).findById(member.getId());
        then(refreshTokenRepository).should(never()).save(any());
    }
}