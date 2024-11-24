package Repository;
import Criteria.Criteria;
import Criteria.Filter;
import Repository.HandleConections.ConnectionDbHibernate;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import Models.Producto;
import org.hibernate.query.Query;

import java.util.List;


// Aqui doy uso de Criteria API de Hibernate para realizar consultas a la base de datos con filtros, orden, límite y desplazamiento segun mi clase Criteria/criterios ,
// aqui no se usan Converters ya que se trata directamente con objetos gracias a la Criteria API
public class RepositoryHibernateCriteriaApi implements IRepository {
    private final Session session;

    public RepositoryHibernateCriteriaApi() {
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

    public List<Producto> all() {
        Query<Producto> Query = createQuery(null);
        return Query.getResultList();
    }

    public List<Producto> matching(Criteria criteria) {
        Query<Producto> Query = createQuery(criteria);

        if (criteria.getLimit() != null) {
            Query.setMaxResults(criteria.getLimit());
        }

        if (criteria.getOffset() != null) {
            Query.setFirstResult(criteria.getOffset());
        }

        return Query.getResultList();
    }

    private Query<Producto> createQuery(Criteria criteria) {
        CriteriaBuilder cb = session.getCriteriaBuilder(); //criteria api bilder
        CriteriaQuery<Producto> CriteriaQuery = cb.createQuery(Producto.class);
        Root<Producto> root = CriteriaQuery.from(Producto.class);

        if (criteria != null) {
            // Crear un Predicate para las condiciones de los filtros
            Predicate predicate = cb.conjunction();  // Empieza con una condición 'verdadera'

            // Agregar los filtros desde el objeto Criteria
            for (Filter filter : criteria.getFilters()) {
                String field = filter.getField();
                Object value = filter.getValue();

                predicate = cb.and(predicate, cb.equal(root.get(field), value));
            }

            CriteriaQuery.where(predicate);

            // Agregar orden
            if (criteria.getOrder() != null) {
                CriteriaQuery.orderBy(serializeOrder(criteria.getOrder(), cb, root));
            }
        }

        return session.createQuery(CriteriaQuery);
    }

    private Order serializeOrder(String order, CriteriaBuilder criteriaBuilder, Root<Producto> root) {
        // Se divide la cadena del orden en dos partes: campo y dirección
        String[] orderParts = order.split(" ");
        String field = orderParts[0];
        String direction = orderParts[1];  // La dirección (ASC o DESC)

        // Verificar si la dirección es ascendente o descendente y construir el objeto Order correspondiente
        if ("ASC".equalsIgnoreCase(direction)) {
            return criteriaBuilder.asc(root.get(field));
        } else if ("DESC".equalsIgnoreCase(direction)) {
            return criteriaBuilder.desc(root.get(field));
        }

        // Si la dirección no es válida, lanzar una excepción o devolver un orden por defecto
        throw new IllegalArgumentException("Invalid order direction: " + direction);
    }
}