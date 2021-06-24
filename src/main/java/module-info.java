module com.starboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens com.starboard to javafx.fxml, com.fasterxml.jackson.databind, javafx.graphics;
    exports com.starboard;

    exports com.starboard.items to com.fasterxml.jackson.databind;
    opens com.starboard.items to com.fasterxml.jackson.databind;
}
