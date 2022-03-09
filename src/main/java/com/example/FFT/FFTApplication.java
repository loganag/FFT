package com.example.FFT;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.mariuszgromada.math.mxparser.*;

import java.io.IOException;

public class FFTApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FFTApplication.class.getResource("mainApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 900);
        stage.getIcons().add(new Image(FFTApplication.class.getResourceAsStream("sine--v1.png")));
        stage.setTitle("Быстрое преобразование Фурье");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();

/*        long time = System.currentTimeMillis();
        Function f1 = new Function("y(x) = cos(x) + sin(2*x)");
        double result = f1.calculate(2);
        System.out.println(result);
        System.out.println(System.currentTimeMillis() - time + "\n\n");

        time = System.currentTimeMillis();
        result = Math.cos(2) + Math.sin(2*2);
        System.out.println(result);
        System.out.println(System.currentTimeMillis() - time);*/
    }

}