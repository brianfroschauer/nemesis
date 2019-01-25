package dao;

import model.Sale;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

/**
 * Author: brianfroschauer
 * Date: 2019-01-07
 */
public class SaleDAO extends AbstractDAO<Sale> {

    /**
     * Get purchases of the user with provided id.
     * In the case of not having any sales, it will return an empty list.
     *
     * @param userId from which the products will be obtained.
     *
     * @return a product list.
     */
    public List<Sale> getUserPurchases(Integer userId) {
        Transaction tx = null;
        final List<Sale> sales;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT sale " +
                               "FROM Sale sale " +
                               "WHERE sale.user.id = :userId";
            final Query<Sale> query = session.createQuery(hql, Sale.class);
            query.setParameter("userId", userId);
            sales = query.list();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return sales;
    }
}