**Spring 23/24 CS102 Final Project**

Contributors:
* Arda Selim Arslan
* Berkant Mahir İleli
* Muhammed Devran Yılmaz
* Rıfat Arifoğlu

Java SDK version: [22.0.1](https://www.oracle.com/tr/java/technologies/downloads/)

External Libraries:
* [JavaFX](https://openjfx.io/)
* [JavaMail](https://javaee.github.io/javamail/#Download_JavaMail_Release)

Maven Dependencies:

    <dependencies>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>22-ea+11</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>22-ea+11</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mongodb</groupId>
            <artifactId>mongodb-driver-sync</artifactId>
            <version>4.11.1</version>
        </dependency>
    </dependencies>

Java Modules:

    module org.rusteze.bilevent {
        requires javafx.controls;
        requires javafx.fxml;
        requires org.mongodb.driver.sync.client;
        requires org.mongodb.bson;
        requires org.mongodb.driver.core;
        requires java.desktop;
        requires javafx.swing;
        requires java.mail;
    
        opens org.rusteze.bilevent to javafx.fxml;
        exports org.rusteze.bilevent;
    }