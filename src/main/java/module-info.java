module lk.ijse.pahasarastudiofp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires protobuf.java; // If you're using JDBC
    // Add other required JavaFX modules (e.g., javafx.graphics, javafx.base)

    opens lk.ijse.pahasarastudiofp.controller to javafx.fxml;
    opens lk.ijse.pahasarastudiofp.dto.tm to javafx.base;
    exports lk.ijse.pahasarastudiofp; // Exports your main application package
    // You might need to export other packages if they are accessed from outside the module
}