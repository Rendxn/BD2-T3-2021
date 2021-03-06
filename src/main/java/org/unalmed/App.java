package org.unalmed;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.unalmed.config.MongoClientInstance;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.unalmed.config.OracleClientInstance;
import org.unalmed.daos.EstadisticaDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



/**
 * JavaFX App
 */
public class App extends Application {

    public App(){
    }

    private static Scene scene;

    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("home"), 1257, 473);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return (Parent)fxmlLoader.load();
    }

    public static void main(String[] args) {
//        MongoClient mongoClient = MongoClientInstance.mongoClient();
//        String databaseName = System.getProperty("mongodb.database");
//        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);

//        Connection conn = OracleClientInstance.oracleClient();
        launch();
    }

}