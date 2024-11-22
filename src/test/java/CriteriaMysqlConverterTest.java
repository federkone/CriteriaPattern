import Criterios.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class CriteriaMysqlConverterTest {
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

        String converterSql = new CriteriaMysqlConverter().convert(criteria);

        String expected = "SELECT * FROM products WHERE 1=1 AND category = 'Electronics' AND price = '1000' ORDER BY price ASC LIMIT 1 OFFSET 0";
        assertEquals(expected, converterSql);
    }
}