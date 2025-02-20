package kz.mathncode.backend.dao;

import java.util.List;
import java.util.UUID;

public interface DAO<T> {
    void create(T entity);
    T readOne(UUID id);
    List<T> readMany(int offset, int limit);
    void update(UUID id, T entity);
    void delete(UUID id);

    List<T> findByField(String field, Object value);
    T findFirstByField(String field, Object value);
}
