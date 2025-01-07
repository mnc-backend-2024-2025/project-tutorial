package kz.mathncode.backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.UUID;

public abstract class AbstractDAO<T> implements DAO<T> {
    private Class<T> entityClass;
    private EntityManager entityManager;

    public AbstractDAO(Class<T> entityClass, EntityManager entityManager) {
        this.entityClass = entityClass;
        this.entityManager = entityManager;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void create(T entity) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(entity);
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public T readOne(UUID id) {
        try {
            getEntityManager().getTransaction().begin();
            T result = getEntityManager().find(entityClass, id);
            getEntityManager().getTransaction().commit();

            return result;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            throw e;
        }
    }

    public String tableName() {
        Metamodel meta = getEntityManager().getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);

        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        return (tableAnnotation == null) ? entityType.getName().toUpperCase() : tableAnnotation.name();
    }

    @Override
    public List<T> readMany(int offset, int limit) {
        try {
            getEntityManager().getTransaction().begin();
            TypedQuery<T> query = getEntityManager().createQuery("""
                    SELECT e
                    FROM :table e
                    ORDER BY id
                    """, entityClass);

            List<T> entities = query.setParameter("table", tableName())
                    .setMaxResults(limit)
                    .setFirstResult(offset)
                    .getResultList();

            getEntityManager().getTransaction().commit();

            return entities;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(UUID id, T entity) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(entity);
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void delete(UUID id) {
        T entity = this.readOne(id);
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(entity);
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            throw e;
        }
    }
}
