package Repository;
import Criterios.Criteria;
import Criterios.CriteriaHQLConverter;
import Criterios.Filter;
import Repository.HandleConections.ConnectionDbHibernate;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import modelos.Producto;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;


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

    // Método para obtener todos los productos
    public List<Producto> all() {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
        Root<Producto> root = query.from(Producto.class);
        query.select(root); // Selecciona todos los campos de Producto

        // Ejecutar la consulta usando la Criteria API
        Query<Producto> hqlQuery = session.createQuery(query);
        return hqlQuery.getResultList();
    }

    // Método para obtener productos según filtros, límite y desplazamiento
    public List<Producto> matching(Criteria criteria) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
        Root<Producto> root = query.from(Producto.class);

        // Crear un Predicate para las condiciones de los filtros
        Predicate predicate = cb.conjunction();  // Empieza con una condición 'verdadera'

        // Agregar los filtros desde el objeto Criteria
        for (Filter filter : criteria.getFilters()) {
            String field = filter.getField();
            Object value = filter.getValue();

            predicate = cb.and(predicate, cb.equal(root.get(field), value));
        }

        query.where(predicate);

        // Agregar orden
        if (criteria.getOrder() != null) {
            query.orderBy(serializeOrder(criteria.getOrder(), cb, root));
        }

        // Configurar límites y desplazamiento
        Query<Producto> hqlQuery = session.createQuery(query);

        if (criteria.getLimit() != null) {
            hqlQuery.setMaxResults(criteria.getLimit());
        }

        if (criteria.getOffset() != null) {
            hqlQuery.setFirstResult(criteria.getOffset());
        }

        // Ejecutar la consulta
        return hqlQuery.getResultList();
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