module com.example.lnulibrary {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires log4j;
    requires java.sql;

    opens com.example.lnulibrary to javafx.fxml;
    exports com.example.lnulibrary;
    exports com.example.lnulibrary.database;
    opens com.example.lnulibrary.database to javafx.fxml;
    exports com.example.lnulibrary.controllers;
    opens com.example.lnulibrary.controllers to javafx.fxml;
}