module org.mpeiRZA {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.mpeiRZA to javafx.fxml;
    exports org.mpeiRZA;
}