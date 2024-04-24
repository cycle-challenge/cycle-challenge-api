package com.yeohangttukttak.api.domain.place.service;

import com.yeohangttukttak.api.domain.bookmark.dao.BookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.dao.PlaceBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlaceFindBookmarkedService {

    private final BookmarkRepository<PlaceBookmark> bookmarkRepository;
    private final MemberRepository memberRepository;
    private final PlaceGetPreviewImageService getPreviewImageService;

    public PlaceFindBookmarkedService(
            PlaceBookmarkRepository bookmarkRepository,
            MemberRepository memberRepository,
            PlaceGetPreviewImageService getPreviewImageService) {
        this.bookmarkRepository = bookmarkRepository;
        this.memberRepository = memberRepository;
        this.getPreviewImageService = getPreviewImageService;
    }

    public List<PlaceDTO> call(String email) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        List<Place> places = bookmarkRepository
                .findAllByMember(member.getId()).stream()
                .map(PlaceBookmark::getTarget)
                .toList();

        return getPreviewImageService.call(places);

    }

}
