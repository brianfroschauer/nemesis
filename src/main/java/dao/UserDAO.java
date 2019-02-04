package dao;

import model.Store;
import model.User;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 28/03/2018
 */
public class UserDAO extends AbstractDAO<User> {

    /**
     * Get a user with provided username, and return an optional user.
     * If the username provided are invalid, it returns an empty optional,
     * otherwise it returns an optional with the user.
     *
     * @param username of the user to be authenticated.
     *
     * @return an optional user.
     */
    public Optional<User> getUserByUsername(String username) {
        Transaction tx = null;
        final User user;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT user " +
                               "FROM User user " +
                               "WHERE user.username = :username";
            final Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);
            user = query.uniqueResult();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return Optional.ofNullable(user);
    }

    /**
     * Get the stores managed by the user with provided id.
     * In the case of not managing any store, it will return an empty list.
     *
     * @param userId from which the stores will be obtained.
     *
     * @return a store list.
     */
    public List<Store> getStoresFromUser(Integer userId) {
        Transaction tx = null;
        final List<Store> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final User user = session.get(User.class, userId);
            list = user.getStores();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return list;
    }

    /**
     * Add an existing store to an existing user, both the
     * store and the user must be persisted in the database.
     *
     * @param userId to whom the store will be added.
     * @param storeId to be added to the user.
     */
    public void addStoreToUser(Integer userId, Integer storeId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final User user = session.get(User.class, userId);
            final Store store = session.get(Store.class, storeId);
            user.addStore(store);
            session.merge(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Delete the store with the specified id from the user with the specified id.
     * This only deletes the store from the user, it does not deleted the store itself.
     *
     * @param userId to whom the store will be deleted.
     * @param storeId to be deleted from the user.
     */
    public void deleteStoreFromUser(Integer userId, Integer storeId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final User user = session.get(User.class, userId);
            final Store store = session.get(Store.class, storeId);
            user.removeStore(store);
            session.merge(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}