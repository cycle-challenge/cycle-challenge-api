package com.yeohangttukttak.api.domain.bookmark.dao;

import com.yeohangttukttak.api.domain.bookmark.entity.TravelBookmark;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class TravelBookmarkRepository extends BookmarkRepository<TravelBookmark> {

    public TravelBookmarkRepository(EntityManager em) {
        super(TravelBookmark.class, em);
    }

}
