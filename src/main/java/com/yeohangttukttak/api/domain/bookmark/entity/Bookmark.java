package com.yeohangttukttak.api.domain.bookmark.entity;

import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.global.interfaces.Bookmarkable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Bookmark<T extends Bookmarkable> {

    @EmbeddedId
    protected BookmarkId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    public abstract T getTarget();

    protected Bookmark(Member member) {
        this.member = member;
    }

}
