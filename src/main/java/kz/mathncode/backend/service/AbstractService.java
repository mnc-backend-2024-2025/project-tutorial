package kz.mathncode.backend.service;

import kz.mathncode.backend.dao.DAO;

public abstract class AbstractService<T> {
    private final DAO<T> dao;

    public AbstractService(DAO<T> dao) {
        this.dao = dao;
    }

    public DAO<T> getDao() {
        return dao;
    }
}
