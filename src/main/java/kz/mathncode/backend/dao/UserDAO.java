package kz.mathncode.backend.dao;

import jakarta.persistence.EntityManager;
import kz.mathncode.backend.entity.User;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
