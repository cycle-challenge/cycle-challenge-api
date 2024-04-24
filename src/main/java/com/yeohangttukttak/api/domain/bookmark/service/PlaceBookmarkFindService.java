package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.BookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.dao.PlaceBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.dto.BookmarkDTO;
import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.dto.PlaceDTO;
import com.yeohangttukttak.api.domain.place.entity.Place;
import com.yeohangttukttak.api.domain.place.service.PlaceGetPreviewImageService;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaceBookmarkFindService extends BookmarkFindService<PlaceBookmark> {

    public PlaceBookmarkFindService(PlaceBookmarkRepository bookmarkRepository,
            MemberRepository memberRepository) {
        super(bookmarkRepository, memberRepository);
    }

}
