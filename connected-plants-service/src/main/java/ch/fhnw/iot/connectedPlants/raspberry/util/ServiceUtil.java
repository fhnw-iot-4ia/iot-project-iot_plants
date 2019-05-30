package ch.fhnw.iot.connectedPlants.raspberry.util;

import ch.fhnw.iot.connectedPlants.raspberry.PlantProperties;
import ch.fhnw.iot.connectedPlants.raspberry.service.Service;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

public class ServiceUtil {

    public static final Properties properties;

    private static final String dbName;

    private static MongoCollection<Document> collection;

    static {
        properties = new Properties();
        try {
            //load a properties file from class path, inside static method
            properties.load(Service.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        dbName = properties.getProperty(PlantProperties.MONGO_DB_NAME);

        // Creating a Mongo client
        MongoClient mongo = new MongoClient("localhost", 27017);

        // Accessing the database
        MongoDatabase database = mongo.getDatabase(dbName);

        // Retrieving a collection collectionName in this case == dbName
        collection = database.getCollection(dbName);
        if (collection == null) {
            throw new IllegalArgumentException(String.format("could not load collection with name: %s", dbName));
        }
    }

    public static Document getDocument() {
        // Getting the iterable object
        FindIterable<Document> iterDoc = collection.find();
        if (iterDoc == null) {
            throw new IllegalArgumentException("could not find DB Access");
        }

        int i = 1;

        Iterator it = iterDoc.iterator();
        Document result = null;

        while (it.hasNext()) {
            result = (Document) it.next();
        }

        return result;
    }

    // Null Werte nicht erlaubt
    public static void saveDocument(Document toSave, double moisture, String mqtt) {
        if (toSave == null) {
            throw new IllegalArgumentException("toSave is not specified");
        }

        if (moisture <= 0.0) {
            throw new IllegalArgumentException("moisture is smaller then 0. This is not possible");
        }

        if (mqtt == null || mqtt.isEmpty()) {
            throw new IllegalArgumentException("mqtt is not specified");
        }

        Object id = toSave.get("_id");

        if (id != null) {
            collection.updateOne(Filters.eq("_id", id), Updates.set("measuredMoistureValue", moisture));
            collection.updateOne(Filters.eq("_id", id), Updates.set("mqtt", mqtt));
        }
    }

    public static void saveDocument(Document toSave, String lastExecuteDate) {
        if (toSave == null) {
            throw new IllegalArgumentException("toSave is not specified");
        }

        if (lastExecuteDate == null || lastExecuteDate.isEmpty()) {
            throw new IllegalArgumentException("lastExecuteDate is not specified");
        }

        Object id = toSave.get("_id");

        if (id != null) {
            collection.updateOne(Filters.eq("_id", id), Updates.set("last", lastExecuteDate));
        }
    }
}
