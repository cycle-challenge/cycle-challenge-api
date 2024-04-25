package com.yeohangttukttak.api.global.interfaces;

import java.io.Serializable;
import java.util.Optional;

public interface BaseRepository<T, I extends Serializable> {

    I save(T entity);

    Optional<T> find(I id);

    void delete(T entity);

}
