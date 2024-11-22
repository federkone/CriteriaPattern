import Criterios.Criteria;
import Criterios.CriteriaMongoDbConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CriteriaMongoDbConverterTest {
    Criteria criteria;
    @BeforeEach
    void setUp() {
        criteria = Criteria.create()
                .filter("category", "Electronics")
                .filter("price", 100)
                .order("price", true)
                .limit(10)
                .offset(5);
    }

    @Test
    void TestCriteriaToMongoDb() {

        String converterMongo = new CriteriaMongoDbConverter().convert(criteria);
       // String mongoQuery = converterMongo.convert(criteria);

        String expectedMongo = "{\"query\": {\"category\": \"Electronics\", \"price\": 100}, \"options\": {\"sort\": {\"price\": 1}, \"limit\": 10, \"skip\": 5}}";
        assertEquals(expectedMongo, converterMongo);

    }
}

