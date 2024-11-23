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



```
### RepostiorySQL se encarga de traducir esos criterios al lenguaje de consulta sql:

```java
public class RepositorySql implements IRepository {
        public List<Producto> matching(Criteria criteria) {
                String querySql = new CriteriaMysqlConverter("products").convert(criteria);
        
                /*
                  CriteriaMysqlConverter retornará la traduccion al lenguaje sql en formato string:
                 "SELECT * FROM products WHERE 1=1 AND category = 'Electronics' AND price = '1000' ORDER BY price ASC LIMIT 1 OFFSET 0"
                */
        
                //y aqui ejecutamos la consulta
                return productos;
        }
}

```

## RepositoryMongoDb se encarga de traducir los criterios a una consulta mongoDb

```java

public class RepositoryMongoDb implements IRepository {
        public List<Producto> matching(Criteria criteria) {
                 String queryMongo = new CriteriaMongoDbConverter().convert(criteria);

                /*
                 CriteriaMongoDbConverter retornará la traduccion a un string compuesto que se usarán como lenguaje comun para mongoDb
                {"query": {"category": "Electronics", "price": 100}, "options": {"sort": {"price": 1}, "limit": 1, "skip": 0}}
                */
                Document queryDocument = Document.parse(queryMongo);
                Document query = queryDocument.get("query", Document.class);
                Document options = queryDocument.get("options", Document.class);

                //y aqui ejecutamos la consulta
                return productos;
        }
}
```
Podremos crear los traductores que necesitemos.
