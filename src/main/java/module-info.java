module btssio.sport {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires org.kordamp.ikonli.core;
    requires java.sql;
    requires mysql.connector.j;

    opens btssio.sport to javafx.fxml;
    exports btssio.sport;
    opens btssio.sport.Equipe to javafx.fxml;
    opens btssio.sport.Competition to javafx.fxml;
    opens btssio.sport.Personne to javafx.fxml;
}