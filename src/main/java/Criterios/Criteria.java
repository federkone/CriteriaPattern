package Criterios;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Criteria {
    private Map<String, Object> filters;
    private String order;
    private Integer limit;
    private Integer offset;

    private Criteria() {
        this.filters = new LinkedHashMap<>();
    }

    public static Criteria create() {
        return new Criteria();
    }

    // Métodos de combinación lógicos estáticos
    /*public static Criteria and(Criteria... criteria) {
        Criteria combined = new Criteria();
        for (Criteria c : criteria) {
            combined.filters.putAll(c.filters);  // Combina los filtros de los Criteria pasados
        }
        return combined;
    }*/

    /*public static Criteria or(Criteria... criteria) {
        Criteria combined = new Criteria();
        // En este caso, para manejar el OR, necesitas manejar la lógica internamente o adaptarla
        for (Criteria c : criteria) {
            for (Map.Entry<String, Object> entry : c.filters.entrySet()) {
                // Puedes manejar la lógica OR aquí
                combined.filters.put(entry.getKey(), entry.getValue());
            }
        }
        return combined;
    }*/

    public Criteria filter(String field, Object value) {
        filters.put(field, value);
        return this;
    }

    public Criteria order(String field, boolean ascending) {
        this.order = field + (ascending ? " ASC" : " DESC");
        return this;
    }

    public Criteria limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Criteria offset(int offset) {
        this.offset = offset;
        return this;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public String getOrder() {
        return order;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }
}