module org.rusteze.bilevent {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires java.desktop;


    opens org.rusteze.bilevent to javafx.fxml;
    exports org.rusteze.bilevent;
}