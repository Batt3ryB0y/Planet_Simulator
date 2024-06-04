module com.example.semestral {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.semestral to javafx.fxml;
    exports com.example.semestral;
}