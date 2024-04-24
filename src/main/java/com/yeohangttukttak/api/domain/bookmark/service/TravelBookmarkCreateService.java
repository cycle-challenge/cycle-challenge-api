package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.TravelBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.entity.TravelBookmark;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.travel.dao.TravelRepository;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import org.springframework.stereotype.Service;

@Service
public class TravelBookmarkCreateService extends BookmarkCreateService<TravelBookmark, Travel> {

    public TravelBookmarkCreateService(
            TravelBookmarkRepository bookmarkRepository,
            MemberRepository memberRepository,
            TravelRepository itemRepository) {
        super(bookmarkRepository, memberRepository, itemRepository, TravelBookmark.class, Travel.class);
    }

}