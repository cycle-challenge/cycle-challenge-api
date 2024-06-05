package com.yeohangttukttak.api.domain.member.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Data
@Embeddable
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class BlockId implements Serializable {

    private Long blockerId;

    private Long blockedId;

}
