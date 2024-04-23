package com.yeohangttukttak.api.domain.bookmark.entity;

import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.travel.entity.Travel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelBookmark extends Bookmark {

    @MapsId("targetId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", insertable = false, updatable = false)
    private Travel target;

    public TravelBookmark(Member member, Travel travel) {
        super(member);
        this.target = travel;
        super.id = new BookmarkId(member.getId(), travel.getId());
    }
}
