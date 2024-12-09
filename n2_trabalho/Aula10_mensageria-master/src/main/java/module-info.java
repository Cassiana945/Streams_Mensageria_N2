module com.mensageria.mensageria_10 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires static lombok;
    requires com.rabbitmq.client;

    opens com.mensageria.mensageria_10 to javafx.fxml;
    exports com.mensageria.mensageria_10;
}