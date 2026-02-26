module javafx.heart_of_the_wildlands {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires jlayer;

    opens heart_of_the_wildlands to javafx.fxml;
    exports heart_of_the_wildlands;
    exports heart_of_the_wildlands.controllers;
    opens heart_of_the_wildlands.controllers to javafx.fxml;
    exports heart_of_the_wildlands.inventory;
    opens heart_of_the_wildlands.inventory to javafx.fxml;
}