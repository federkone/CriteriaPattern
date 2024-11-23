Ejemplo de uso, donde creo un filtrado"criteria", utilizo el repositorioSQL
y buscamos obtener la lista de objetos"productos" que hagan match con los criterios definidos
 
        criteria = Criteria.create()
                .filter("category", "Electronics")
                .filter("price", 1000)
                .order("price", true)
                .limit(1)
                .offset(0);

        repository = new RepositorySql();

         List<Producto> productos = repository.matching(criteria);

         
