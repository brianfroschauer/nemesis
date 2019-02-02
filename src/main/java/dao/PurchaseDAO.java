package dao;

import model.Purchase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

/**
 * Author: brianfroschauer
 * Date: 2019-01-07
 */
public class PurchaseDAO extends AbstractDAO<Purchase> {

    /**
     * Get purchases of the user with provided id.
     * In the case of not having any sales, it will return an empty list.
     *
     * @param userId from which the products will be obtained.
     *
     * @return a product list.
     */
    public List<Purchase> getUserPurchases(Integer userId) {
        Transaction tx = null;
        final List<Purchase> purchases;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT purchase " +
                               "FROM Purchase purchase " +
                               "WHERE purchase.user.id = :userId";
            final Query<Purchase> query = session.createQuery(hql, Purchase.class);
            query.setParameter("userId", userId);
            purchases = query.list();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return purchases;
    }
}