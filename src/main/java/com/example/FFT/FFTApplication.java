package com.example.FFT;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FFTApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FFTApplication.class.getResource("mainApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 900);
        stage.setTitle("Быстрое преобразование Фурье");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

    public static Complex[] vectorPreparation(double initValue, double finalValue, double sampling)
    {
        double x = initValue;
        int n = (int) Math.floor((finalValue-initValue)/sampling) + 1;
        System.out.println(n);

/*        //Проверка на степень 2-ки
        int j = 1;
        while (n>j) {
            j<<=1;
        }
        if(j == n) System.out.println("true");
        else System.out.println("false");
        */

        int newN = (int) Math.pow(2, Math.ceil(Math.log(29)/Math.log(2)));     //Приведение к степени 2-ки
        System.out.println(newN);


        Complex[] arr = new Complex[newN];
        //arr[0] = new Complex(Math.cos(initValue) + Math.sin(2*initValue), 0);
        for (int i = 0; i < newN; i++) {
            System.out.println(x);
            arr[i] = new Complex(Math.cos(x) + Math.sin(2*x), 0);
            x += sampling;
        }
        //FFT.show(arr);
        return arr;
    }
}