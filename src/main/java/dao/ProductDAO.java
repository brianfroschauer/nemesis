package dao;

import model.Product;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

import java.util.List;

/**
 * Author: brianfroschauer
 * Date: 11/04/2018
 */
public class ProductDAO extends AbstractDAO<Product> {

    /**
     * Search products that matches with the specified key.
     * If there are no products that match the key provided,
     * an empty list will be returned.
     *
     * @param key to be matched.
     *
     * @return a list of matching products.
     */
    public List<Product> searchProducts(String key) {
        Transaction tx = null;
        final List<Product> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT product " +
                               "FROM Product product " +
                               "WHERE product.name LIKE :key";
            final Query<Product> query = session.createQuery(hql, Product.class);
            query.setParameter("key", "%" + key + "%");
            list = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return list;
    }
}