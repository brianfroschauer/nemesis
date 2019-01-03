package dao;

import model.Product;
import model.Store;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 11/04/2018
 */
public class StoreDAO extends AbstractDAO<Store> {

    public Optional<Store> getStore(String name) {
        Transaction tx = null;
        final Store store;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT store " +
                    "FROM Store store " +
                    "WHERE store.name = :name";
            final Query<Store> query = session.createQuery(hql, Store.class);
            query.setParameter("name", name);
            store = query.uniqueResult();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return Optional.ofNullable(store);
    }

    /**
     * Get products from the store where their category matches the category id provided.
     * If the parameters provided do not match any product, an empty list will be returned.
     *
     * @param storeId from which the products will be searched.
     * @param categoryId to be searched in the products.
     *
     * @return a list of products.
     */
    public List<Product> getProductsFromStore(Integer storeId, Integer categoryId) {
        Transaction tx = null;
        final List<Product> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT products " +
                               "FROM Store store JOIN store.products products " +
                               "WHERE store.id = :storeId AND products.category.id = :categoryId";
            final Query<Product> query = session.createQuery(hql, Product.class);
            query.setParameter("storeId", storeId);
            query.setParameter("categoryId", categoryId);
            list = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return list;
    }

    /**
     * Get products from the store with the specified id.
     * If the store does not any product, an empty list will be returned.
     *
     * @param storeId from which the products will be obtained.
     *
     * @return a list of products.
     */
    public List<Product> getProductsFromStore(Integer storeId) {
        Transaction tx = null;
        final List<Product> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final Store store = session.get(Store.class, storeId);
            list = store.getProducts();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return list;
    }

    /**
     * Search stores that matches the key provided.
     * If the key provided do not match any stores,
     * an empty list will be returned.
     *
     * @param key to be searched in the store names.
     *
     * @return a list of stores.
     */
    public List<Store> searchStores(String key) {
        Transaction tx = null;
        final List<Store> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT store " +
                               "FROM Store store " +
                               "WHERE store.name LIKE :key";
            final Query<Store> query = session.createQuery(hql, Store.class);
            query.setParameter("key", "%" + key + "%").list();
            list = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return list;
    }

    /**
     * Search products from the store with the specified id.
     *
     * @param storeId from which the products will be searched.
     * @param key to be search in the product names.
     *
     * @return a list of products.
     */
    public List<Product> searchProductsFromStore(Integer storeId, String key) {
        Transaction tx = null;
        final List<Product> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT products " +
                               "FROM Store store JOIN store.products products " +
                               "WHERE store.id = :storeId AND products.name LIKE :key";
            final Query<Product> query = session.createQuery(hql, Product.class);
            query.setParameter("key", "%" + key + "%");
            query.setParameter("storeId", storeId);
            list = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return list;
    }

    /**
     * Add an existing product to an existing store, both the
     * product and the store must be persisted in the database.
     *
     * @param storeId to whom the product will be added.
     * @param productId to be added to the store.
     */
    public void addProductToStore(Integer storeId, Integer productId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final Store store = session.get(Store.class, storeId);
            final Product product = session.get(Product.class, productId);
            store.addProduct(product);
            session.merge(store);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}