module gherabijava.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;

    opens com.ayoub.gherabijava.models to javafx.base;
    opens com.ayoub.gherabijava to javafx.fxml;
    exports com.ayoub.gherabijava;
}
