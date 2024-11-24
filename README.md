## Información

![Status](https://img.shields.io/badge/Status-In%20Progress-yellow)
![Version](https://img.shields.io/badge/Version-1.0.0-blue)
![License](https://img.shields.io/badge/License-GPL%20v3-blue.svg)
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen)

El patrón Criteria está diseñado para resolver el problema de la explosión de métodos en los repositorios al manejar múltiples combinaciones de consultas o filtros.
Este patrón actúa como una capa de abstracción que permite definir criterios de búsqueda de forma flexible y reutilizable.
Se utiliza como una capa adicional entre la lógica del negocio y la base de datos, asegurando que los criterios de búsqueda se definan de forma uniforme, portátil y extensible.

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

/*Podemos observar que este diseño nos permite abstraernos de toda implementacion
y de mantener el mismo formato de criterio para cualquier tipo de repositorio*/



```
### RepostiorySQL usa CriteriaMysqlConverter para traducir los criterios al lenguaje de consulta sql:

```java
public class RepositorySql implements IRepository {
        public List<Producto> matching(Criteria criteria) {
                String querySql = new CriteriaMysqlConverter("products").convert(criteria);
        
                /*
                  CriteriaMysqlConverter retornará la traduccion al lenguaje sql en formato string:
                  "SELECT * FROM products WHERE 1=1 AND category = ? AND price = ? ORDER BY price ASC LIMIT 1 OFFSET 0"
                */
        
                //y aqui ejecutamos la consulta
                return productos;
        }
}

```

## RepositoryMongoDb usa CriteriaMongoDbConverter para traducir los criterios a una consulta mongoDb

```java

public class RepositoryMongoDb implements IRepository {
        public List<Producto> matching(Criteria criteria) {
                 String queryMongo = new CriteriaMongoDbConverter().convert(criteria);

                /*
                 CriteriaMongoDbConverter retornará la traduccion a un string compuesto que se usarán como lenguaje comun para mongoDb
                {"query": {"category": "Electronics", "price": 1000}, "options": {"sort": {"price": 1}, "limit": 1, "skip": 0}}
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

![Atención](https://img.shields.io/badge/Atención-Importante-red)

En los casos de utilizar Criteria api con hibernate no es necesario realizar un traductor ya que Criteria Api lo hace por su cuenta.
Esto es solo un ejemplo de como se podria implementar un traductor de Criteria a formato de consulta SQL, Hql, MongoDb, etc. 


