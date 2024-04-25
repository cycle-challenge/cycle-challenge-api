package com.yeohangttukttak.api.domain.bookmark.entity;

import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.place.entity.Place;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceBookmark extends Bookmark<Place> {

    @MapsId("targetId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", insertable = false, updatable = false)
    private Place target;

    public PlaceBookmark(Member member, Place place) {
        super(member);
        this.target = place;
        super.id = new BookmarkId(member.getId(), place.getId());
    }

}
