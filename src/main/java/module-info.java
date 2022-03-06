module com.example.lab1_osi {
    requires javafx.controls;
    requires javafx.fxml;
    requires MathParser.org.mXparser;


    opens com.example.FFT to javafx.fxml;
    exports com.example.FFT;
}