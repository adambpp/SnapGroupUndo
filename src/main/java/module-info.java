module org.adam.gui.asn4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;


    opens org.adam.gui.asn4 to javafx.fxml;
    exports org.adam.gui.asn4;
}