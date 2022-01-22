module com {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires transitive javafx.controls;
    requires org.json;
    opens com.controllers;
    opens com.classes;
    opens com.util;
    exports com;
}
