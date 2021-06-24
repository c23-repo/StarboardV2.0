module com.gui{
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens com.gui to javafx.fxml, com.fasterxml.jackson.databind, javafx.graphics;
    opens com.starboard to com.fasterxml.jackson.databind;
    exports com.gui;
    exports com.starboard to com.fasterxml.jackson.databind;
    opens com.starboard.items to com.fasterxml.jackson.databind;
}
