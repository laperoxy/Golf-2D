module com.example.domaci1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.domaci1 to javafx.fxml;
    exports com.example.domaci1;
}