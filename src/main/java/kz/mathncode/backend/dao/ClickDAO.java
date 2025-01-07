package kz.mathncode.backend.dao;

import jakarta.persistence.EntityManager;
import kz.mathncode.backend.entity.Click;

public class ClickDAO extends AbstractDAO<Click> {
    public ClickDAO(EntityManager entityManager) {
        super(Click.class, entityManager);
    }
}
