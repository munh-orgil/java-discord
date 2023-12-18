module org.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens org.example.javafx to javafx.fxml;
    exports org.example.javafx;
}