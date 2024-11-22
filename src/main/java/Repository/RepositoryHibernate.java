package Repository;
import Criterios.Criteria;
import Criterios.CriteriaMysqlConverter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import modelos.Producto;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.util.List;

public class RepositoryHibernate implements IRepository {

    private final SessionFactory sessionFactory;
    private CriteriaMysqlConverter CriteriaHibernateConverter;

    public RepositoryHibernate() {
        // Configuración de Hibernate
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml") // Archivo de configuración
                .addAnnotatedClass(Producto.class) // Entidad anotada
                .buildSessionFactory();
    }

    @Override
    public List<Producto> all() {
        try (Session session = sessionFactory.openSession()) {

            // Ejecutar consulta nativa
            NativeQuery<Producto> query = session.createNativeQuery("SELECT * FROM products WHERE 1=1", Producto.class);  // no estoy aprovechando el tipo de consulta HQL
            return query.getResultList();
        }
    }
    public List<Producto> matching(Criteria criteria) {
        try (Session session = sessionFactory.openSession()) {
            // Convertir Criteria a SQL
            CriteriaMysqlConverter converter = new CriteriaMysqlConverter();
            String sqlQuery = converter.convert(criteria);

            // Ejecutar consulta nativa
            NativeQuery<Producto> query = session.createNativeQuery(sqlQuery, Producto.class); // no estoy aprovechando el tipo de consulta HQL
            return query.getResultList();
        }
    }
    public void close() {
        sessionFactory.close();
    }
}