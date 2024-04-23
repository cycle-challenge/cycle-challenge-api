package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.BookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.dao.PlaceBookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlaceBookmarkFindService {

    private final BookmarkRepository<PlaceBookmark> bookmarkRepository;
    private final MemberRepository memberRepository;

    public PlaceBookmarkFindService (
            PlaceBookmarkRepository bookmarkRepository,
            MemberRepository memberRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.memberRepository = memberRepository;
    }

    public List<PlaceBookmark> call(String email) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        return bookmarkRepository.findAllByMember(member.getId()).stream().toList();

    }

}
