package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
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

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberSignUpServiceTest {

    @InjectMocks
    private MemberSignUpService memberSignUpService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Password mockPassword;

    @Test
    public void 회원가입_성공_케이스() throws Exception {

        try (MockedStatic<Password> mocked = mockStatic(Password.class)) {

            // given
            String email = "test@example.com";
            String plainText = "test1234";
            String nickname = "test-user";

            mocked.when(() -> Password.create(plainText)).thenReturn(mockPassword);

            given(memberRepository.findByEmail(email)).willReturn(Optional.empty());
            given(memberRepository.findByNickname(nickname)).willReturn(Optional.empty());

            // when
            memberSignUpService.local(email, plainText, nickname);

            // then
            then(memberRepository).should(times(1)).save(any(Member.class));
            mocked.verify(() -> Password.create(plainText), times(1));
        }

    }

    @Test
    public void 이메일_중복_케이스() throws Exception {

        try (MockedStatic<Password> mocked = mockStatic(Password.class)) {

            // given
            String email = "test@example.com";
            String plainText = "test1234";
            String nickname = "test-user";

            given(memberRepository.findByEmail(email)).willReturn(Optional.of(Member.builder().build()));
            mocked.when(() -> Password.create(plainText)).thenReturn(mockPassword);

            // when + then
            assertThatThrownBy(() -> memberSignUpService.local(email, plainText, nickname))
                    .as("Should throw ApiException when email is duplicated")
                    .isInstanceOf(ApiException.class)
                    .hasMessageContaining(ApiErrorCode.DUPLICATED_EMAIL.name());

            then(memberRepository).should(never()).save(any(Member.class));
            mocked.verify(() -> Password.create(plainText), never());

        }

    }

    @Test
    public void 닉네임_중복_케이스() throws Exception {

        try (MockedStatic<Password> mocked = mockStatic(Password.class)) {
            // given
            String email = "test@example.com";
            String plainText = "test1234";
            String nickname = "test-user";
            mocked.when(() -> Password.create(plainText)).thenReturn(mockPassword);

            given(memberRepository.findByEmail(email)).willReturn(Optional.empty());
            given(memberRepository.findByNickname(nickname)).willReturn(Optional.of(Member.builder().build()));

            // when + then
            assertThatThrownBy(() -> memberSignUpService.local(email, plainText, nickname))
                    .as("Should throw ApiException when nickname is duplicated")
                    .isInstanceOf(ApiException.class)
                    .hasMessageContaining(ApiErrorCode.DUPLICATED_NICKNAME.name());

            then(memberRepository).should(never()).save(any(Member.class));
            mocked.verify(() -> Password.create(plainText), never());

        }

    }

}