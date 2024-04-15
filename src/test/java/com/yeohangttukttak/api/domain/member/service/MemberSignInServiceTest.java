package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.entity.JwtToken;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.member.entity.Password;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberSignInServiceTest {

    @InjectMocks
    private MemberSignInService memberSignInService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private Password password;

    @Mock
    private JwtToken mockToken;

    @Test
    public void 정상_케이스() throws Exception {

        try (MockedStatic<JwtToken> mocked = mockStatic(JwtToken.class)) {
            // given
            String email = "test@example.com";
            String plainPassword = "test1234";

            mocked.when(() -> JwtToken.issueAccessToken(any(), any())).thenReturn(mockToken);
            mocked.when(() -> JwtToken.issueRefreshToken(any(), any())).thenReturn(mockToken);

            Member member = Member.builder()
                    .email(email)
                    .password(password)
                    .build();

            given(memberRepository.findByEmail(email))
                    .willReturn(Optional.of(member));
            given(password.validate(plainPassword)).willReturn(true);

            // when
            memberSignInService.local(email, plainPassword);

            // then
            then(refreshTokenRepository).should(times(1)).save(any());

            mocked.verify(() -> JwtToken.issueAccessToken(any(), any()), times(1));
            mocked.verify(() -> JwtToken.issueRefreshToken(any(), any()), times(1));

        }

    }

    @Test
    public void 존재하지_않는_회원() throws Exception {

        try (MockedStatic<JwtToken> mocked = mockStatic(JwtToken.class)) {

            // given
            String email = "test@example.com";
            String plainPassword = "test1234";

            given(memberRepository.findByEmail(email))
                    .willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(
                    () -> memberSignInService.local(email, plainPassword))
                    .isInstanceOf(ApiException.class)
                    .hasMessageContaining(ApiErrorCode.SIGN_IN_FAILED.name());

            then(password).should(never()).validate(plainPassword);
            then(refreshTokenRepository).should(never()).save(any());

            mocked.verify(() -> JwtToken.issueAccessToken(any(), any()), never());
            mocked.verify(() -> JwtToken.issueRefreshToken(any(), any()), never());

        }

    }

    @Test
    public void 비밀번호_불일치() throws Exception {

        try (MockedStatic<JwtToken> mocked = mockStatic(JwtToken.class)) {

            // given
            String email = "test@example.com";
            String plainPassword = "test1234";

            Member member = Member.builder()
                    .email(email)
                    .password(password)
                    .build();

            given(memberRepository.findByEmail(email))
                    .willReturn(Optional.of(member));
            given(password.validate(plainPassword)).willReturn(false);

            // when, then
            assertThatThrownBy(
                    () -> memberSignInService.local(email, plainPassword))
                    .isInstanceOf(ApiException.class)
                    .hasMessageContaining(ApiErrorCode.SIGN_IN_FAILED.name());

            then(refreshTokenRepository).should(never()).save(any());

            mocked.verify(() -> JwtToken.issueAccessToken(any(), any()), never());
            mocked.verify(() -> JwtToken.issueRefreshToken(any(), any()), never());

        }

    }

}