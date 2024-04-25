package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.PlaceBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class PlaceBookmarkDeleteService extends BookmarkDeleteService<PlaceBookmark> {

    public PlaceBookmarkDeleteService(PlaceBookmarkRepository bookmarkRepository,
             MemberRepository memberRepository) {
        super(bookmarkRepository, memberRepository);
    }
}