package dao;

import model.Category;
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

    public List<Category> getUsedCategories(Integer storeId) {
        Transaction tx = null;
        final List<Category> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT DISTINCT product.category " +
                               "FROM Category category JOIN Product product " +
                               "ON category.id = product.category.id AND product.store.id = :storeId";

            final Query<Category> query = session.createQuery(hql, Category.class);
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