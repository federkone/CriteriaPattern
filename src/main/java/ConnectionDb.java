import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ConnectionDb {
    private final SessionFactory sessionFactory;

    public ConnectionDb() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
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
