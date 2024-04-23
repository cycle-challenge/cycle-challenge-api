package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.BookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.dao.PlaceBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.entity.BookmarkId;
import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaceBookmarkDeleteService {

    private final BookmarkRepository<PlaceBookmark> bookmarkRepository;

    private final MemberRepository memberRepository;

    public PlaceBookmarkDeleteService (
            PlaceBookmarkRepository bookmarkRepository,
            MemberRepository memberRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.memberRepository = memberRepository;
    }

    public void call(String email, Long targetId) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        BookmarkId bookmarkId = new BookmarkId(member.getId(), targetId);

        PlaceBookmark placeBookmark = bookmarkRepository.find(bookmarkId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.DUPLICATED_BOOKMARK));

        bookmarkRepository.delete(placeBookmark);
    }

}