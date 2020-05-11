module org.mpeiRZA {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.xml.bind;
    requires lombok;

    opens org.mpeiRZA to javafx.fxml;
    exports org.mpeiRZA;
}
