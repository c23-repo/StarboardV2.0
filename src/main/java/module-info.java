module com.gui{
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens com.gui to javafx.fxml, com.fasterxml.jackson.databind, javafx.graphics;
    exports com.gui;
    opens com.starboard.items to com.fasterxml.jackson.databind;
}
