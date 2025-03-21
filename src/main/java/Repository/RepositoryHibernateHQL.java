package Repository;
import Criteria.Criteria;
import Criteria.CriteriaHQLConverter;
import Criteria.Filter;
import Repository.HandleConections.ConnectionDbHibernate;
import org.hibernate.Session;
import Models.Producto;
import org.hibernate.query.Query;

import java.util.List;

//Aquí doy uso de HQL/String con hibernate para realizar consultas a la base de datos con filtros, orden, límite y desplazamiento según mi clase Criteria/criterios para ello doy uso del CriteriaHQLConverter
public class RepositoryHibernateHQL implements IRepository {
    private final Session session;

    public RepositoryHibernateHQL() {
        this.session = new ConnectionDbHibernate().getSession(); //conectar a la base de datos
    }

    @Override
    public void insertData(Producto producto) {
        session.beginTransaction();
        session.persist(producto);
        session.getTransaction().commit();
    }

    @Override
    public void deleteData(Producto producto) {
        session.beginTransaction();
        session.remove(producto);
        session.getTransaction().commit();
    }

    @Override
    public void updateData(Producto producto) {
        session.beginTransaction();
        session.merge(producto);
        session.getTransaction().commit();
    }

    @Override
    public List<Producto> all() {
            String hqlQuery = "FROM Producto";
            Query<Producto> query = session.createQuery(hqlQuery, Producto.class);
            return query.getResultList();
    }

    public List<Producto> matching(Criteria criteria) {
            // Convertir Criteria a HQL
             String CriteriaQuery = new CriteriaHQLConverter("Producto").convert(criteria);

            // Ejecutar consulta hql
            Query<Producto> query = session.createQuery(CriteriaQuery, Producto.class);

            for (Filter filter : criteria.getFilters()) {
                query.setParameter(filter.getField(), filter.getValue());  //seteo de parametros con los valores de los filtros
            }

            if (criteria.getLimit() != null) {
                query.setMaxResults(criteria.getLimit());
            }

            if (criteria.getOffset() != null) {
                query.setFirstResult(criteria.getOffset());
            }

            return query.getResultList();
    }
}