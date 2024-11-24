import Criteria.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CriteriaHQLConverterTest {
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
    void TestConvertCriteriaToSql() {

        String converterSql = new CriteriaHQLConverter("products").convert(criteria); //sobre la table/collection products se aplica el criterio y retorna la consulta sql

        String expected = "FROM products p WHERE 1=1 AND p.category = :category AND p.price = :price ORDER BY p.price ASC";

        System.out.println("Esperado: " + expected );
        System.out.println("Actual: " + converterSql);
        assertEquals(expected, converterSql);
    }
}