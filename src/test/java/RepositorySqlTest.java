import Criteria.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Repository.IRepository;
import Models.Producto;
import Repository.RepositorySql;

import java.sql.SQLException;
import java.util.List;

public class RepositorySqlTest {
    Criteria criteria;
    IRepository repository;

    @BeforeEach
    void setup() throws SQLException {
        criteria = Criteria.create()
                .filter("category", "Electronics")
                .filter("price", 1000)
                .order("price", true)
                .limit(1)
                .offset(0);

        repository = new RepositorySql();
    }

  @Test
    void TestMatching()  {
        List<Producto> productos = repository.matching(criteria);

        assertEquals(1, productos.size());
        assertEquals("Producto{nombre='Laptop', categoria='Electronics', precio=1000.0, disponible=true}", productos.get(0).toString());
    }

    @Test
    void TestAll()  {
        List<Producto> productos = repository.all();

        assertEquals(5, productos.size());
    }

    @Test
    void TestInsertData() {
        Producto producto = new Producto("Tablet", "Electronics", 500, true);
        repository.insertData(producto);
        List<Producto> productos = repository.all();
        assertEquals(6, productos.size());
        repository.deleteData(producto);
    }

    @Test
    void testUpdateData() {
        Producto producto = new Producto("Laptop", "Computers", 1000, true);
        repository.updateData(producto);
        List<Producto> productos = repository.matching(Criteria.create().filter("category", "Computers"));
        assertEquals(1, productos.size());
        producto.setCategory("Electronics");
        repository.updateData(producto);
    }

}
