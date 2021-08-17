package org.unalmed.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoClientInstance {

    /**
     *
     * @return MongoClient
     */
    public static MongoClient mongoClient() {

        try {
            String uri = System.getProperty("mongodb.uri");
            ConnectionString connString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).build();
            MongoClient mongoClient = MongoClients.create(settings);

            return mongoClient;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
