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

    public List<Item> getItemsFromUser(Integer userId) {
        Transaction tx = null;
        final List<Item> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();

            final String hql = "SELECT item " +
                               "FROM Item item " +
                               "WHERE item.user.id = :userId";
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

    public void deleteAllItemsFromUser(Integer userId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT item " +
                               "FROM Item item " +
                               "WHERE item.user.id = :userId";
            final Query<Item> query = session.createQuery(hql, Item.class);
            query.setParameter("userId", userId);
            final List<Item> items = query.list();
            for (Item item : items) session.delete(item);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Optional<Item> getItemFromUser(Integer userId, Integer productId) {
        Transaction tx = null;
        final Optional<Item> optionalItem;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT item " +
                               "FROM Item item " +
                               "WHERE item.user.id = :userId AND " +
                               "item.product.id = :productId";
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
}
