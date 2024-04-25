package com.yeohangttukttak.api.domain.travel.service;

import com.yeohangttukttak.api.domain.bookmark.dao.TravelBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.entity.TravelBookmark;
import com.yeohangttukttak.api.domain.bookmark.service.FindBookmarkedEntityService;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import org.springframework.stereotype.Service;

@Service
public class TravelFindBookmarkedService extends FindBookmarkedEntityService<TravelBookmark, Travel> {

    public TravelFindBookmarkedService(
            TravelBookmarkRepository bookmarkRepository,
            MemberRepository memberRepository) {
        super(bookmarkRepository, memberRepository);
    }

}
