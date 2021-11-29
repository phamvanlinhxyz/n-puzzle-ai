module com.example.npuzzleai {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.npuzzleai to javafx.fxml;
    exports com.example.npuzzleai;
}