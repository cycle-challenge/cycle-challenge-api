package com.yeohangttukttak.api.domain.member.dao;

import com.yeohangttukttak.api.domain.member.entity.Block;
import com.yeohangttukttak.api.domain.member.entity.BlockId;
import com.yeohangttukttak.api.global.interfaces.BaseRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlockRepository implements BaseRepository<Block, BlockId> {

    private final EntityManager em;

    @Override
    public BlockId save(Block entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Override
    public Optional<Block> find(BlockId id) {
        Block block = em.find(Block.class, id);
        return Optional.ofNullable(block);
    }

    @Override
    public void delete(Block entity) {
        em.remove(entity);
    }

}
