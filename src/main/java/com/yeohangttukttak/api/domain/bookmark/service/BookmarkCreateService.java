package com.yeohangttukttak.api.domain.bookmark.service;

import com.yeohangttukttak.api.domain.bookmark.dao.BookmarkRepository;
import com.yeohangttukttak.api.domain.bookmark.dto.BookmarkDTO;
import com.yeohangttukttak.api.domain.bookmark.entity.Bookmark;
import com.yeohangttukttak.api.domain.bookmark.entity.BookmarkId;
import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import com.yeohangttukttak.api.global.interfaces.BaseRepository;
import com.yeohangttukttak.api.global.interfaces.Bookmarkable;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
@Transactional
public abstract class BookmarkCreateService<B extends Bookmark, T extends Bookmarkable> {

    private final BookmarkRepository<B> bookmarkRepository;

    private final MemberRepository memberRepository;

    private final BaseRepository<T, Long> itemRepository;

    private final Class<B> bookmarkType;
    private final Class<T> targetType;

    public BookmarkDTO call(String email, Long targetId) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        T target = itemRepository.find(targetId).orElseThrow(() ->
                new ApiException(ApiErrorCode.BOOKMARK_TARGET_NOT_FOUND));

        bookmarkRepository.find(new BookmarkId(member.getId(), target.getId()))
                .ifPresent(bookmark -> {
                    throw new ApiException(ApiErrorCode.DUPLICATED_BOOKMARK); });

        BookmarkId createdId = bookmarkRepository.save(create(member, target));

        B createdBookmark = bookmarkRepository.find(createdId)
                .orElseThrow(() -> new ApiException(ApiErrorCode.INTERNAL_SERVER_ERROR));

        return new BookmarkDTO(createdBookmark);
    }

    private B create(Member member, T target) {

        try {

            return bookmarkType.getConstructor(Member.class, targetType).newInstance(member, target);

        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {

            throw new ApiException(ApiErrorCode.INTERNAL_SERVER_ERROR);

        }
    }

}