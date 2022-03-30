package com.example.FFT;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private double initValue;
    private double finalValue;
    private double samplingRate;

    private String function;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public TextField initTextField;

    @FXML
    public TextField finalTextField;

    @FXML
    public TextField sampling;

    @FXML
    private Button buildButton;

    @FXML
    private TextField functionTextField;

    @FXML
    private LineChart<Number, Number> analogChart;

    @FXML
    private LineChart<Number, Number> discreteChart;

    @FXML
    private LineChart<Number, Number> quantizedChart;

    @FXML
    private LineChart<Number, Number> fftChart;

    @FXML
    void buildButtonClicked(MouseEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка ввода данных!");
        alert.setAlertType(Alert.AlertType.WARNING);
        alert.setContentText("Введите корректные данные.");

        //Проверка всех TextField на пустой ввод
        for (Node component: anchorPane.getChildren())
        {
            if(component instanceof TextInputControl)
                if(((TextInputControl) component).getText().equals(""))
                {
                    alert.showAndWait();
                    return;
                }
        }

        //Проверка частоты дискретизации на корректность
        if(Double.parseDouble(sampling.getText()) <= 0)
        {
            alert.showAndWait();
            return;
        }

        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.getData().removeAll(Collections.singleton(analogChart.getData().setAll()));
        dataSeries.getData().removeAll(Collections.singleton(discreteChart.getData().setAll()));
        dataSeries.getData().removeAll(Collections.singleton(quantizedChart.getData().setAll()));
        dataSeries.getData().removeAll(Collections.singleton(fftChart.getData().setAll()));

        function = "y(x)=" + functionTextField.getText();
        System.out.println(function);
        initValue = Double.parseDouble(initTextField.getText());
        finalValue = Double.parseDouble(finalTextField.getText());
        samplingRate = Double.parseDouble(sampling.getText());

        analogChart.getData().add(CalculatingGraphs.getAnalogDataSeries(function, initValue, finalValue, 0.01));

        CalculatingGraphs.buildingDiscreteGraph(discreteChart, function, initValue, finalValue, samplingRate);

        quantizedChart.getData().add(CalculatingGraphs.getAnalogDataSeries(function, initValue, finalValue, 0.01));

        CalculatingGraphs.buildingQuantizedGraph(quantizedChart, function, initValue, finalValue, samplingRate);

        CalculatingGraphs.buildingFFTGraph(fftChart, function, initValue, finalValue, samplingRate);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NumberAxis xAxisAnalog = (NumberAxis) analogChart.getXAxis();
        NumberAxis yAxisAnalog = (NumberAxis) analogChart.getYAxis();
        xAxisAnalog.setLabel("Время (сек)");
        yAxisAnalog.setLabel("Амплитуда");
        analogChart.setCreateSymbols(false);

        NumberAxis xAxisDiscrete = (NumberAxis) discreteChart.getXAxis();
        NumberAxis yAxisDiscrete = (NumberAxis) discreteChart.getYAxis();
        xAxisDiscrete.setLabel("Время (сек)");
        yAxisDiscrete.setLabel("Амплитуда");
        discreteChart.setCreateSymbols(false);
        discreteChart.setLegendVisible(false);


        NumberAxis xAxisQuantized = (NumberAxis) quantizedChart.getXAxis();
        NumberAxis yAxisQuantized = (NumberAxis) quantizedChart.getYAxis();
        xAxisQuantized.setLabel("Время (сек)");
        yAxisQuantized.setLabel("Амплитуда");
        quantizedChart.setCreateSymbols(false);
        quantizedChart.setLegendVisible(false);

        NumberAxis xAxisFFT = (NumberAxis) fftChart.getXAxis();
        NumberAxis yAxisFFT = (NumberAxis) fftChart.getYAxis();
        xAxisFFT.setLabel("Частота (Гц)");
        yAxisFFT.setLabel("Магнитуда");
        fftChart.setLegendVisible(false);

    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(anchorPane.getScene().getWindow());
        int n = 0;
        try (var reader = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()))) {
            while (reader.readLine() != null) {
                n++;
            }
        }
        catch (IOException e){
            System.out.println("File read error");
        }

        int newN = (int) Math.pow(2, Math.ceil(Math.log(n)/Math.log(2)));     //Приведение к степени 2-ки

        Complex[] complexArray = new Complex[newN];

        try(BufferedReader in = new BufferedReader(new FileReader(selectedFile.getAbsolutePath())))
        {
            String str;
            int i = 0;
            while((str = in.readLine()) != null){
                String[] tokens = str.split(",");
                complexArray[i] = new Complex(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]));
                i++;
            }
        }
        catch (IOException e){
            System.out.println("File read error");
        }

        for (int i = 0; i < newN; i++) {
            if(complexArray[i] == null)
                complexArray[i] = new Complex(0, 0);
        }

        FFT.show(complexArray);


        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.getData().removeAll(Collections.singleton(fftChart.getData().setAll()));
        dataSeries.getData().removeAll(Collections.singleton(analogChart.getData().setAll()));
        dataSeries.getData().removeAll(Collections.singleton(discreteChart.getData().setAll()));
        dataSeries.getData().removeAll(Collections.singleton(quantizedChart.getData().setAll()));

        analogChart.getData().add(CalculatingGraphs.getAnalogDataSeries(complexArray, newN));
        CalculatingGraphs.buildingFFTGraph(fftChart, complexArray, newN, n);
        CalculatingGraphs.buildingDiscreteGraph(discreteChart, complexArray, newN);

    }

    public void saveFile(ActionEvent actionEvent)
    {
        function = "y(x)=" + functionTextField.getText();
        initValue = Double.parseDouble(initTextField.getText());
        finalValue = Double.parseDouble(finalTextField.getText());
        samplingRate = Double.parseDouble(sampling.getText());

        Complex[] outArray = FFT.vectorPreparation(function, initValue, finalValue, samplingRate);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(anchorPane.getScene().getWindow());

        try {
            PrintWriter writer = new PrintWriter("output.txt", StandardCharsets.UTF_8);
            for (Complex item:outArray) {
                writer.println(Double.toString(item.re()) + "\t" +Double.toString(item.im()));
                System.out.println(item.re() + "\t" + item.im());
            }
            writer.close();

        }
        catch (IOException e)
        {
            System.out.println("Can't write to file");
        }

    }
}