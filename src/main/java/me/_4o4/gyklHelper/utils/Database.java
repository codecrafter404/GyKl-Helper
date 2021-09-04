package me._4o4.gyklHelper.utils;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import me._4o4.gyklHelper.models.Environment;
import me._4o4.gyklHelper.models.Server;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

public class Database {


    public Server getServerFromID(@NotNull String serverID) {
        MongoClient mongo = MongoClients.create(String.format("mongodb://%s:%s@%s:27017",
                Environment.getMongoUsername(),
                Environment.getMongoPassword(),
                Environment.getMongoContainer()
        ));
        MongoCollection<Document> collection = mongo.getDatabase("GyKl-Helper").getCollection("Server");

        Document toSearch = new Document("server_id", serverID);
        Document result = collection.find(toSearch).cursor().hasNext() ? (Document) collection.find(toSearch).cursor().next() : null;
        return result == null ? null : new Gson().fromJson(result.toJson(), Server.class);
    }
    public void updateServer(@NotNull String serverID, @NotNull Server server){
        MongoClient mongo = MongoClients.create(String.format("mongodb://%s:%s@%s:27017",
                Environment.getMongoUsername(),
                Environment.getMongoPassword(),
                Environment.getMongoContainer()
        ));
        MongoCollection<Document> collection = mongo.getDatabase("GyKl-Helper").getCollection("Server");

        Document toSave = Document.parse(new Gson().toJson(server));
        Document toReplace = new Document("server_id", serverID);
        Document update = new Document("$set", toSave);

        collection.updateOne(toReplace, update);
    }
    public void saveServer(@NotNull Server server) {
        MongoClient mongo = MongoClients.create(String.format("mongodb://%s:%s@%s:27017",
                    Environment.getMongoUsername(),
                    Environment.getMongoPassword(),
                    Environment.getMongoContainer()
                ));
        MongoCollection<Document> collection = mongo.getDatabase("GyKl-Helper").getCollection("Server");

        Document toSave = Document.parse(new Gson().toJson(server));
        collection.insertOne(toSave);
    }
}
