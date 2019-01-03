package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 29/03/2018
 */
public class AbstractDAO<T> implements DAO<T> {

    @Override
    public Integer create(@NotNull T entity) {
        Transaction tx = null;
        final Integer id;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            id = (Integer) session.save(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T update(@NotNull T entity) {
        Transaction tx = null;
        final T updatedEntity;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            updatedEntity = (T) session.merge(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return updatedEntity;
    }

    @Override
    public void delete(@NotNull T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Optional<T> get(Class<T> entityType, Integer id) {
        Transaction tx = null;
        final Optional<T> optionalEntity;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final T entity = session.get(entityType, id);
            optionalEntity = Optional.ofNullable(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return optionalEntity;
    }

    @Override
    public List<T> getAll(Class<T> entityType) {
        Transaction tx = null;
        final List<T> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "FROM " + entityType.getSimpleName();
            list = session.createQuery(hql, entityType).list();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return list;
    }
}