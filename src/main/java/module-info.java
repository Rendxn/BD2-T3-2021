module org.unalmed {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    exports org.unalmed;
    exports org.unalmed.controllers;
    opens org.unalmed.controllers to javafx.fxml;
}