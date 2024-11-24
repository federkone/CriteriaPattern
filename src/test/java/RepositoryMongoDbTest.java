import Criteria.Criteria;

import Repository.IRepository;

import Models.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import Repository.RepositoryMongoDb;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepositoryMongoDbTest {
    Criteria criteria;
    IRepository repository;

    @BeforeEach
    void setup(){
        criteria = Criteria.create()
                .filter("category", "Electronics")
                .filter("price", 1200)
                .order("price", true)
                .limit(1)
                .offset(0);

        repository = new RepositoryMongoDb();
    }

    @Test
    void TestMatching() {
        List<Producto> productos = repository.matching(criteria);

        assertEquals(1, productos.size());
        assertEquals("Producto{nombre='Laptop Dell XPS 13', categoria='Electronics', precio=1200.0, disponible=true}", productos.get(0).toString());
    }

    @Test
    void TestAll() {
        List<Producto> productos = repository.all();

        assertEquals(10, productos.size());
    }

    @Test
    void TestInsertData() {
        Producto producto = new Producto("Tablet", "Electronics", 500, true);
        repository.insertData(producto);
        List<Producto> productos = repository.all();
        assertEquals(11, productos.size());
        repository.deleteData(producto);
    }

    @Test
    void testUpdateData() {
        Producto producto = new Producto("Laptop Dell XPS 13", "Computers", 1200, true);
        repository.updateData(producto);
        List<Producto> productos = repository.matching(Criteria.create().filter("category", "Computers"));
        assertEquals(1, productos.size());
        producto.setCategory("Electronics");
        repository.updateData(producto);
    }
}
