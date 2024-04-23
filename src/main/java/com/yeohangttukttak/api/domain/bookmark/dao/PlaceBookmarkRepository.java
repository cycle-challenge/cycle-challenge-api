package com.yeohangttukttak.api.domain.bookmark.dao;

import com.yeohangttukttak.api.domain.bookmark.entity.PlaceBookmark;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PlaceBookmarkRepository extends BookmarkRepository<PlaceBookmark> {

    public PlaceBookmarkRepository(EntityManager em) {
        super(PlaceBookmark.class, em);
    }

}
