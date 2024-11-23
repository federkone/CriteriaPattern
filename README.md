### Ejemplo de uso: Filtrado con Criteria y Repository

```java
// Crear criterios de búsqueda, con todos los filtrios que deseamos aplicar "desde 0..n filtros"
Criteria criteria = Criteria.create()
        .filter("category", "Electronics") // Filtrar por categoría
        .filter("price", 1000)             // Filtrar por precio
        .order("price", true)             // Ordenar por precio de forma ascendente
        .limit(1)                         // Limitar a un resultado
        .offset(0);                       // Iniciar en el primer resultado

// Crear instancia del repositorio
IRepository repositorySql = new RepositorySql();
IRepository repositoryMongodb = new RepositoryMongoDb();

// Obtener la lista de productos que coincidan con los criterios
List<Producto> productosSql = repositorySql.matching(criteria); 
List<Producto> productosMongodb = repositoryMongoDb.matching(criteria);

//Podemos observar que este diseño nos permite abstraernos de toda implementacion
//y de mantener el mismo formato de criterio para cualquier tipo de repositorio

```plaintext
### Ejemplo de uso: Filtrado con Criteria y Repository
```java
 String querySql = new CriteriaMysqlConverter("products").convert(criteria);
