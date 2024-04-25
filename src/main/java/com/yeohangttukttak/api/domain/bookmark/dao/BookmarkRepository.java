package com.yeohangttukttak.api.domain.bookmark.dao;

import com.yeohangttukttak.api.domain.bookmark.entity.Bookmark;
import com.yeohangttukttak.api.domain.bookmark.entity.BookmarkId;
import com.yeohangttukttak.api.global.interfaces.BaseRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class BookmarkRepository <T extends Bookmark> implements BaseRepository<T, BookmarkId> {

    private final Class<T> type;

    private final EntityManager em;

    public BookmarkRepository(Class<T> type, EntityManager em) {
        this.type = type;
        this.em = em;
    }

    @Override
    public BookmarkId save(T bookmark) {
        em.persist(bookmark);
        return bookmark.getId();
    }

    @Override
    public Optional<T> find(BookmarkId id) {
        return Optional.ofNullable(em.find(type, id));
    }

    @Override
    public void delete(T bookmark) {
        em.remove(bookmark);
    }

    public List<T> findAllByMember(Long memberId) {
        return em.createQuery(
                "SELECT b FROM " + type.getName() + " as b " +
                        "WHERE b.id.memberId = :memberId", type)
                .setParameter("memberId", memberId)
                .getResultList();
    }

}
