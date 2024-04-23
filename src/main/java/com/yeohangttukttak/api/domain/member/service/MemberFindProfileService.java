package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dto.MemberProfileDTO;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFindProfileService {

    private final MemberRepository memberRepository;

    public MemberProfileDTO findByEmail(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        return new MemberProfileDTO(member);

    }

}
