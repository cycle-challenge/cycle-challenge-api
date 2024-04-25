package com.yeohangttukttak.api.domain.place.service;

import com.yeohangttukttak.api.domain.bookmark.dao.PlaceBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import com.yeohangttukttak.api.domain.bookmark.service.FindBookmarkedEntityService;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.place.entity.Place;
import org.springframework.stereotype.Service;

@Service
public class PlaceFindBookmarkedService extends FindBookmarkedEntityService<PlaceBookmark, Place> {

    public PlaceFindBookmarkedService(
            PlaceBookmarkRepository bookmarkRepository,
            MemberRepository memberRepository) {
        super(bookmarkRepository, memberRepository);
    }
}
