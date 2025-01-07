package kz.mathncode.backend.dao;

import jakarta.persistence.EntityManager;
import kz.mathncode.backend.entity.URLResource;

public class URLResourceDAO extends AbstractDAO<URLResource> {
    public URLResourceDAO(EntityManager entityManager) {
        super(URLResource.class, entityManager);
    }
}
