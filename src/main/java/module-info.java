/**
 *
 */
module org.mpeiRZA {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.xml.bind;
    requires lombok;
    requires com.sun.xml.bind;
    requires annotations;


    opens org.mpeiRZA to javafx.fxml;
    opens Services to java.xml.bind, javafx.base;
    opens org.mpeiRZA.iec61850 to javafx.base;
    exports org.mpeiRZA;
    exports org.mpeiRZA.Controllers;
}
