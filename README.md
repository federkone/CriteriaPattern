### Ejemplo de uso: Filtrado con Criteria y RepositorySql

```java
// Crear criterios de búsqueda, con todos los filtrios que deseamos aplicar "desde 0..n filtros"
Criteria criteria = Criteria.create()
        .filter("category", "Electronics") // Filtrar por categoría
        .filter("price", 1000)             // Filtrar por precio
        .order("price", true)             // Ordenar por precio de forma ascendente
        .limit(1)                         // Limitar a un resultado
        .offset(0);                       // Iniciar en el primer resultado

// Crear instancia del repositorio
RepositorySql repository = new RepositorySql();

// Obtener la lista de productos que coincidan con los criterios
List<Producto> productos = repository.matching(criteria); 
