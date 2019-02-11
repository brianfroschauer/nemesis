package dao;

import model.Item;
import model.Product;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 2019-02-02
 */
public class ItemDAO extends AbstractDAO<Item> {

    /**
     * Gets an specified item from user cart.
     *
     * @param userId from which the item will be obtained.
     * @param productId of the specific item.
     * @return an optional Item.
     */
    public Optional<Item> getItemFromUser(Integer userId, Integer productId) {
        Transaction tx = null;
        final Optional<Item> optionalItem;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT item " +
                               "FROM Item item " +
                               "WHERE item.user.id = :userId AND " +
                               "item.product.id = :productId AND " +
                               "item.active = true";
            final Query<Item> query = session.createQuery(hql, Item.class);
            query.setParameter("userId", userId);
            query.setParameter("productId", productId);
            final Item item = query.uniqueResult();
            optionalItem = Optional.ofNullable(item);
            tx.commit();
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return optionalItem;
    }

    /**
     * Get the items of the user with provided id.
     *
     * @param userId from which the products will be obtained.
     * @return a product list.
     */
    public List<Item> getItemsFromUser(Integer userId) {
        Transaction tx = null;
        final List<Item> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT item " +
                               "FROM Item item " +
                               "WHERE item.user.id = :userId " +
                               "AND item.active = true";
            final Query<Item> query = session.createQuery(hql, Item.class);
            query.setParameter("userId", userId);
            list = query.list();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return list;
    }

    /**
     * Add an existing product to an existing user, both the
     * store and the user must be persisted in the database.
     *
     * @param userId to whom the product will be added.
     * @param productId to be added to the user.
     * @param quantity product amount.
     */
    public void addItemToUser(Integer userId, Integer productId, Integer quantity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final User user = session.get(User.class, userId);
            final Product product = session.get(Product.class, productId);
            final Item item = new Item(user, product, quantity);
            session.save(item);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Delete the product with the specified id from the user with the specified id.
     * This only deletes the product from the user cart, it does not deleted the product itself.
     *
     * @param userId from which the product will be deleted.
     * @param productId to be deleted from the user.
     */
    public void deleteItemFromUser(Integer userId, Integer productId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT item " +
                               "FROM Item item " +
                               "WHERE item.user.id = :userId AND item.product.id = :productId";
            final Query<Item> query = session.createQuery(hql, Item.class);
            query.setParameter("userId", userId);
            query.setParameter("productId", productId);
            Item item = query.getSingleResult();
            session.delete(item);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    /**
     * Delete all products from the user with the specified id.
     *
     * @param userId from which the products will be deleted.
     */
    public void deleteAllItemsFromUser(Integer userId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "DELETE FROM Item item WHERE item.user.id = :userId";
            final Query query = session.createQuery(hql);
            query.setParameter("userId", userId).executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}
