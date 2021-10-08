package me._4o4.gyklHelper.utils;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import me._4o4.gyklHelper.models.Environment;
import me._4o4.gyklHelper.models.Server;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Database {


    /**
     * Gets the server from the database using a ID
     *
     * @param serverID the id of the server
     * @return the server object from the database, if not found: null
     */
    public Server getServerFromID(@NotNull String serverID) {
        MongoClient mongo = MongoClients.create(String.format("mongodb://%s:%s@%s:27017",
                Environment.getMongoUsername(),
                Environment.getMongoPassword(),
                Environment.getMongoContainer()
        ));
        MongoCollection<Document> collection = mongo.getDatabase("GyKl-Helper").getCollection("Server");

        Document toSearch = new Document("server_id", serverID);
        Document result = collection.find(toSearch).cursor().hasNext() ? (Document) collection.find(toSearch).cursor().next() : null;
        mongo.close();
        return result == null ? null : new Gson().fromJson(result.toJson(), Server.class);
    }

    /**
     * This method updates the server object in the database
     *
     * @param serverID the id of the server
     * @param server the replacement
     */
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
        mongo.close();
    }

    /**
     * This method deletes a server from the database
     *
     * @param serverID server to delete
     */
    public void deleteServer(@NotNull String serverID){
        MongoClient mongo = MongoClients.create(String.format("mongodb://%s:%s@%s:27017",
                Environment.getMongoUsername(),
                Environment.getMongoPassword(),
                Environment.getMongoContainer()
        ));
        MongoCollection<Document> collection = mongo.getDatabase("GyKl-Helper").getCollection("Server");
        collection.deleteOne(
                new Document("server_id", serverID)
        );
        mongo.close();
    }

    /**
     * This method saves a server
     *
     * @param server server to save
     */
    public void saveServer(@NotNull Server server) {
        MongoClient mongo = MongoClients.create(String.format("mongodb://%s:%s@%s:27017",
                    Environment.getMongoUsername(),
                    Environment.getMongoPassword(),
                    Environment.getMongoContainer()
                ));
        MongoCollection<Document> collection = mongo.getDatabase("GyKl-Helper").getCollection("Server");

        Document toSave = Document.parse(new Gson().toJson(server));
        collection.insertOne(toSave);
        mongo.close();
    }

    /**
     * Fetch all servers from the database
     *
     * @return a list of the founded servers
     */
    public List<Server> getAllServers(){
        MongoClient mongo = MongoClients.create(String.format("mongodb://%s:%s@%s:27017",
                Environment.getMongoUsername(),
                Environment.getMongoPassword(),
                Environment.getMongoContainer()
        ));
        MongoCollection<Document> collection = mongo.getDatabase("GyKl-Helper").getCollection("Server");
        MongoCursor <Document> cursor = collection.find().cursor();

        List<Server> result = new ArrayList<>();

        while(cursor.hasNext()){
            result.add(
                    new Gson().fromJson(cursor.next().toJson(), Server.class)
            );
        }
        mongo.close();
        return result;
    }
}
