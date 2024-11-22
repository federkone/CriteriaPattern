package Criterios;


import java.util.Map;
import org.bson.Document;

public class CriteriaMongoDbConverter implements ICriteriaConverter {

    @Override
    public String convert(Criteria criteria) {
        Document query = new Document();

        // Agregar filtros
        for (Map.Entry<String, Object> filter : criteria.getFilters().entrySet()) {
            query.append(filter.getKey(), filter.getValue());
        }

        Document options = new Document();

        // Agregar orden
        if (criteria.getOrder() != null) {
            String[] orderParts = criteria.getOrder().split(" ");
            options.append("sort", new Document(orderParts[0], orderParts[1].equalsIgnoreCase("ASC") ? 1 : -1));
        }

        // Agregar límite
        if (criteria.getLimit() != null) {
            options.append("limit", criteria.getLimit());
        }

        // Agregar offset
        if (criteria.getOffset() != null) {
            options.append("skip", criteria.getOffset());
        }

        return new Document("query", query).append("options", options).toJson();
    }
}