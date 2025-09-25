module com.isikchatting.isikchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;

    opens com.isikchatting.isikchat to javafx.fxml;
    opens com.isikchatting.isikchat.controller to javafx.fxml;
    opens com.isikchatting.isikchat.view to javafx.fxml;

    exports com.isikchatting.isikchat;
}