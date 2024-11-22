package Criterios;



import java.util.Map;

public class CriteriaMysqlConverter implements ICriteriaConverter {

    @Override
    public String convert(Criteria criteria) {
        StringBuilder query = new StringBuilder("SELECT * FROM products WHERE 1=1");

        // Agregar filtros
        for (Map.Entry<String, Object> filter : criteria.getFilters().entrySet()) {
            query.append(" AND ").append(filter.getKey()).append(" = '").append(filter.getValue()).append("'");
        }

        // Agregar orden
        if (criteria.getOrder() != null) {
            query.append(" ORDER BY ").append(criteria.getOrder());
        }

        // Agregar límite
        if (criteria.getLimit() != null) {
            query.append(" LIMIT ").append(criteria.getLimit());
        }

        // Agregar offset
        if (criteria.getOffset() != null) {
            query.append(" OFFSET ").append(criteria.getOffset());
        }

        return query.toString();
    }
}