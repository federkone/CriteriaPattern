import Criterios.*;
import static org.junit.jupiter.api.Assertions.*;

import Repository.IRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Repository.RepositoryHibernate;
import modelos.Producto;

import java.sql.SQLException;
import java.util.List;

public class RepositoryHibernateTest {
    Criteria criteria;
    @BeforeEach
    void setup(){
        criteria = Criteria.create()
                .filter("category", "Electronics")
                .filter("price", 1000)
                .order("price", true)
                .limit(1)
                .offset(0);
    }

    @Test
    void TestMatching() throws SQLException {
        IRepository repository = new RepositoryHibernate();
        List<Producto> productos = repository.matching(criteria);

        assertEquals(1, productos.size());
        assertEquals("Producto{nombre='Laptop', categoria='Electronics', precio=1000.0, disponible=true}", productos.get(0).toString());
    }
    @Test
    void TestAll() throws SQLException {
        IRepository repository = new RepositoryHibernate();
        List<Producto> productos = repository.all();

        assertEquals(5, productos.size());
    }

}