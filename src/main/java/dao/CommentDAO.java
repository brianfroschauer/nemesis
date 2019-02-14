package dao;

import model.Comment;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

/**
 * Author: brianfroschauer
 * Date: 2019-02-14
 */
public class CommentDAO extends AbstractDAO<Comment> {

    public List<Comment> getCommentsFromProduct(Integer productId) {
        Transaction tx = null;
        final List<Comment> list;
        try (Session session = HibernateUtil.openSession()) {
            tx = session.beginTransaction();
            final String hql = "SELECT comment " +
                               "FROM Comment comment " +
                               "WHERE comment.product.id = :productId";
            final Query<Comment> query = session.createQuery(hql, Comment.class);
            query.setParameter("productId", productId);
            list = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
        return list;
    }
}
