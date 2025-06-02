module lk.ijse.pahasarastudiofp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires protobuf.java;


    requires java.desktop;
    requires java.logging;
    requires java.xml;


    requires java.mail;
    opens lk.ijse.pahasarastudiofp.controller to javafx.fxml;
    opens lk.ijse.pahasarastudiofp.dto.tm to javafx.base;
    exports lk.ijse.pahasarastudiofp;

}
