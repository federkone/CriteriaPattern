package Repository.HandleConections;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionDbMongo {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public ConnectionDbMongo() {
        Dotenv dotenv = Dotenv.load();
        String uri = dotenv.get("MONGODB_URI");
        String dbName = dotenv.get("DB_NAME");

        // Conectar a MongoDB
        this.mongoClient = MongoClients.create(uri);
        this.database = mongoClient.getDatabase(dbName);
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}