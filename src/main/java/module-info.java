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


    opens org.mpeiRZA to javafx.fxml;
    opens Services to java.xml.bind;
    exports org.mpeiRZA;
    exports org.mpeiRZA.Controllers;
}
