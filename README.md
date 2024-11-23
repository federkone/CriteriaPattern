## Ejemplo de uso, donde creo un filtrado"criteria", utilizo el repositorioSQL
## y buscamos obtener la lista de objetos"productos" que hagan match con los criterios definidos
 
```java
// Crear criterios de búsqueda
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

// Imprimir los productos
productos.forEach(System.out::println);
