package util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Author: brianfroschauer
 * Date: 24/03/2018
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (HibernateException exception) {
            System.err.println("Error creating Session: " + exception);
            throw new ExceptionInInitializerError(exception);
        }
    }

    public static Session openSession(){
        return sessionFactory.openSession();
    }
}
