package Repository;

import Criteria.Criteria;
import Criteria.CriteriaMongoDbConverter;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import Models.Producto;
import Repository.HandleConections.ConnectionDbMongo;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

//Aqui doy uso de MongoQuery/string para realizar consultas a la base de datos mognoDb con filtros, orden, límite y desplazamiento
//segun mi clase Criteria/criterios para ello doy uso del CriteriaMongoDbConverter para convertir los criterios a una consulta mongoDb
public class RepositoryMongoDb implements IRepository {
    private MongoDatabase database;

    public RepositoryMongoDb() {
        database = new ConnectionDbMongo().getDatabase();  //conectar a la base de datos
    }

    @Override
    public void insertData(Producto producto) {
        MongoCollection<Document> collection = database.getCollection("products");
        Document doc = new Document("name", producto.getName())
                .append("category", producto.getCategory())
                .append("price", producto.getPrice())
                .append("available", producto.isAvailable());
        collection.insertOne(doc);
    }

    @Override
    public void deleteData(Producto producto) {
        MongoCollection<Document> collection = database.getCollection("products");
        Document doc = new Document("name", producto.getName());
        collection.deleteOne(doc);
    }

    @Override
    public void updateData(Producto producto) {
        MongoCollection<Document> collection = database.getCollection("products");
        Document filter = new Document("name", producto.getName());
        Document update = new Document("$set", new Document("category", producto.getCategory())
                .append("price", producto.getPrice())
                .append("available", producto.isAvailable()));
        collection.updateOne(filter, update);
    }

    @Override
    public List<Producto> all() {
        List<Producto> productos =new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("products");
        FindIterable<Document> result = collection.find();
        for (Document doc : result) {
            // Mapear el documento a un objeto Producto
            String nombre = doc.getString("name");
            String categoria = doc.getString("category");
            Double precio = doc.getDouble("price");
            boolean disponible = doc.getBoolean("available", false);

            // Crear un objeto Producto y agregarlo a la lista
            productos.add(new Producto(nombre, categoria, precio, disponible));
        }
        return productos;
    }

    @Override
    public List<Producto> matching(Criteria criteria) {
        List<Producto> productos =new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("products");

        String queryMongo = new CriteriaMongoDbConverter().convert(criteria);  //obtengo consulta segun criteria
        Document queryDocument = Document.parse(queryMongo);

        Document query = queryDocument.get("query", Document.class);//tengo que extraer el query y options
        Document options = queryDocument.get("options", Document.class);

        FindIterable<Document> result = collection.find(query)
                .sort(options.get("sort", Document.class)) //para ordenar
                .limit(options.getInteger("limit",0)) //para limitar la cantidad de elementos
                .skip(options.getInteger("skip",0));  //para saltar los primeros n elementos

        for (Document doc : result) {
            // Mapear el documento a un objeto Producto
            String nombre = doc.getString("name");
            String categoria = doc.getString("category");
            Double precio = doc.getDouble("price");
            boolean disponible = doc.getBoolean("available", false);

            // Crear un objeto Producto y agregarlo a la lista
            productos.add(new Producto(nombre, categoria, precio, disponible));  //serializo la respuesta para convertir a objetos
        }


        return productos;
    }
}
