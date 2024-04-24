package com.yeohangttukttak.api.domain.bookmark.dto;

import com.yeohangttukttak.api.domain.bookmark.entity.Bookmark;
import lombok.Data;

@Data
public class BookmarkDTO {

    private Long targetId;

    public BookmarkDTO(Bookmark bookmark) {
        this.targetId = bookmark.getId().getTargetId();;
    }

}
