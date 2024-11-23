package Criterios;



import java.util.Map;

public class CriteriaMysqlConverter implements ICriteriaConverter {
    private final String tableName;

    public CriteriaMysqlConverter(String tableName) {
        this.tableName = tableName;
    }
    @Override
    public String convert(Criteria criteria) {
        StringBuilder query = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE 1=1");

        // Agregar filtros
        for (Filter filter : criteria.getFilters()) {
            query.append(" AND ").append(filter.getField()).append(" = '").append(filter.getValue()).append("'");
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