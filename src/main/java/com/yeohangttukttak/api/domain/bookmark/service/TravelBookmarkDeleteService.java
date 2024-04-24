package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.TravelBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.entity.TravelBookmark;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class TravelBookmarkDeleteService extends BookmarkDeleteService<TravelBookmark> {

    public TravelBookmarkDeleteService(TravelBookmarkRepository bookmarkRepository,
                                       MemberRepository memberRepository) {
        super(bookmarkRepository, memberRepository);
    }
}