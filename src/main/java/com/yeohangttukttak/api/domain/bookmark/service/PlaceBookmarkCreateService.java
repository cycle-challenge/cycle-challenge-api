package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.BookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.dao.PlaceBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.dto.BookmarkDTO;
import com.yeohangttukttak.api.domain.bookmark.entity.BookmarkId;
import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaceBookmarkCreateService {

    private final BookmarkRepository<PlaceBookmark> bookmarkRepository;

    private final MemberRepository memberRepository;

    private final PlaceRepository itemRepository;

    public PlaceBookmarkCreateService(
            PlaceBookmarkRepository bookmarkRepository,
            MemberRepository memberRepository,
            PlaceRepository itemRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

    public BookmarkDTO call(String email, Long targetId) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        Place place = itemRepository.find(targetId).orElseThrow(() ->
                new ApiException(ApiErrorCode.BOOKMARK_TARGET_NOT_FOUND));

        bookmarkRepository.find(new BookmarkId(member.getId(), place.getId()))
                .ifPresent(bookmark -> {
                    throw new ApiException(ApiErrorCode.DUPLICATED_BOOKMARK); });

        BookmarkId createdId = bookmarkRepository.save(new PlaceBookmark(member, place));

        PlaceBookmark createdBookmark = bookmarkRepository.find(createdId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.INTERNAL_SERVER_ERROR));

        return new BookmarkDTO(createdBookmark);
    }

}