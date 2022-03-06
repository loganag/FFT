module com.example.lab1_osi {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.FFT to javafx.fxml;
    exports com.example.FFT;
}