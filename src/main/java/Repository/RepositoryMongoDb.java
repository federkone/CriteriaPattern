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
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public RepositoryMongoDb() {
        database = new ConnectionDbMongo().getDatabase();  //conectar a la base de datos
        collection = database.getCollection("products"); //obtener coleccion
    }

    @Override
    public void insertData(Producto producto) {
        Document doc = new Document("name", producto.getName())
                .append("category", producto.getCategory())
                .append("price", producto.getPrice())
                .append("available", producto.isAvailable());
        collection.insertOne(doc);
    }

    @Override
    public void deleteData(Producto producto) {
        Document doc = new Document("name", producto.getName());
        collection.deleteOne(doc);
    }

    @Override
    public void updateData(Producto producto) {
        Document filter = new Document("name", producto.getName());
        Document update = new Document("$set", new Document("category", producto.getCategory())
                .append("price", producto.getPrice())
                .append("available", producto.isAvailable()));
        collection.updateOne(filter, update);
    }

    @Override
    public List<Producto> all() {
        FindIterable<Document> result = collection.find();
        return getProductos(result);
    }

    @Override
    public List<Producto> matching(Criteria criteria) {
        String queryMongo = new CriteriaMongoDbConverter().convert(criteria);  //obtengo consulta segun criteria

        Document queryDocument = Document.parse(queryMongo);
        Document query = queryDocument.get("query", Document.class);//extraer query y options
        Document options = queryDocument.get("options", Document.class);

        FindIterable<Document> result = collection.find(query)
                .sort(options.get("sort", Document.class))
                .limit(options.getInteger("limit",0))
                .skip(options.getInteger("skip",0));

        return getProductos(result);
    }

    private List<Producto> getProductos(FindIterable<Document> result) {
        List<Producto> productos =new ArrayList<>();
        for (Document doc : result) {
            // Mapear el documento a un objeto Producto
            String name = doc.getString("name");
            String category = doc.getString("category");
            Double price = doc.getDouble("price");
            boolean available = doc.getBoolean("available");

            productos.add(new Producto(name, category, price, available));
        }
        return productos;
    }
}
