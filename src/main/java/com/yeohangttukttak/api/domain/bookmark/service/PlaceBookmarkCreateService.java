package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.PlaceBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.place.dao.PlaceRepository;
import com.yeohangttukttak.api.domain.place.entity.Place;
import org.springframework.stereotype.Service;

@Service
public class PlaceBookmarkCreateService extends BookmarkCreateService<PlaceBookmark, Place> {

    public PlaceBookmarkCreateService(
            PlaceBookmarkRepository bookmarkRepository,
            MemberRepository memberRepository,
            PlaceRepository itemRepository) {
        super(bookmarkRepository, memberRepository, itemRepository, PlaceBookmark.class, Place.class);
    }

}