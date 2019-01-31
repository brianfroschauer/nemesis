package dao;

import model.Category;
import model.Product;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

/**
 * Author: brianfroschauer
 * Date: 2019-01-07
 */
public class CategoryDAO extends AbstractDAO<Category> {

    public List<Product> getUsedCategories(Integer storeId) {
        Transaction tx = null;
        final List<Product> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT DISTINCT product.category " +
                    "FROM Category category JOIN Product product JOIN Store store " +
                    "WHERE category.id = product.category.id AND product.store.id = :storeId";

            final Query<Product> query = session.createQuery(hql, Product.class);
            query.setParameter("storeId", storeId);
            list = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return list;
    }

}
