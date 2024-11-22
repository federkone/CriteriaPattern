import Criterios.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Repository.Repository;
import modelos.Producto;

import java.util.List;

public class RepositoryQueryTest {
    Criteria criteria;
    @BeforeEach
    void setup(){
        criteria = Criteria.create()
                .filter("category", "Electronics")
                .filter("price", 1000)
                .order("price", true)
                .limit(10)
                .offset(5);
    }

  @Test
    void TestMatching() {
        Repository repository = new Repository();
        List<Producto> productos = repository.matching(criteria);

        assertEquals(1, productos.size());
    }
    @Test
    void TestAll() {
        Repository repository = new Repository();
        List<Producto> productos = repository.all();

        assertEquals(5, productos.size());
    }

}
