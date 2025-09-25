module com.example.digitallibrary {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens com.example.digitallibrary.model.entities to org.hibernate.orm.core;
    opens com.example.digitallibrary.controller to javafx.fxml;
    opens com.example.digitallibrary to javafx.fxml;

    exports com.example.digitallibrary.controller to javafx.fxml;
    exports com.example.digitallibrary;
}