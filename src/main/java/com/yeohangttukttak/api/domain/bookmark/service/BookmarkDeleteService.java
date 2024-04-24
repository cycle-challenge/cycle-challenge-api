package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.BookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.dto.BookmarkDTO;
import com.yeohangttukttak.api.domain.bookmark.entity.Bookmark;
import com.yeohangttukttak.api.domain.bookmark.entity.BookmarkId;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public abstract class BookmarkDeleteService<B extends Bookmark> {

    private final BookmarkRepository<B> bookmarkRepository;

    private final MemberRepository memberRepository;

    public BookmarkDTO call(String email, Long targetId) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        BookmarkId bookmarkId = new BookmarkId(member.getId(), targetId);

        B bookmark = bookmarkRepository.find(bookmarkId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);

        return new BookmarkDTO(bookmark);
    }

}