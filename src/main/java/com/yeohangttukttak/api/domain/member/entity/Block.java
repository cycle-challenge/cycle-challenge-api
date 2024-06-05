package com.yeohangttukttak.api.domain.member.entity;

import com.yeohangttukttak.api.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Block extends BaseEntity {

    @EmbeddedId
    private BlockId id;

    @MapsId("blockerId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "blocker_id", insertable = false, updatable = false)
    private Member blocker;

    @MapsId("blockedId")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "blocked_id", insertable = false, updatable = false)
    private Member blocked;

    public Block(Member blocker, Member blocked) {
        this.blocker = blocker;
        this.blocked = blocked;
        this.id = new BlockId(blocker.getId(), blocked.getId());
    }
}
