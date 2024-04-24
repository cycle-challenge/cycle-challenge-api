package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.BookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.dto.BookmarkDTO;
import com.yeohangttukttak.api.domain.bookmark.entity.Bookmark;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkFindService<B extends Bookmark> {

    private final BookmarkRepository<B> bookmarkRepository;
    private final MemberRepository memberRepository;

    public List<BookmarkDTO> call(String email) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        return bookmarkRepository.findAllByMember(member.getId()).stream()
                .map(BookmarkDTO::new).toList();

    }

}
