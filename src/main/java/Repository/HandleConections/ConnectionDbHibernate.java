package Repository.HandleConections;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ConnectionDbHibernate {
    private SessionFactory sessionFactory;

    public ConnectionDbHibernate() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory(); //bildea la sesion con los parametros del archivo hibernate.cfg.xml
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
