package com.example.FFT;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
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

        function = "y(x)=" + functionTextField.getText();
        System.out.println(function);
        initValue = Double.parseDouble(initTextField.getText());
        finalValue = Double.parseDouble(finalTextField.getText());
        samplingRate = Double.parseDouble(sampling.getText());

        analogChart.getData().add(CalculatingGraphs.getAnalogDataSeries(function, initValue, finalValue, 0.01));

        CalculatingGraphs.buildingDiscreteGraph(discreteChart, function, initValue, finalValue, samplingRate);

        quantizedChart.getData().add(CalculatingGraphs.getAnalogDataSeries(function, initValue, finalValue, 0.01));
        //CalculatingGraphs.buildingQuantizedGraph(quantizedChart, "y(x)=cos(x-0.1)+sin(2*x-0.1)",0, 8, 0.1);
        CalculatingGraphs.buildingQuantizedGraph(quantizedChart, function, initValue, finalValue, samplingRate);

        CalculatingGraphs.buildingFFTGraph(fftChart, function, initValue, finalValue, samplingRate);


/*        analogChart.getData().add(CalculatingGraphs.getAnalogDataSeries("y(x)=cos(x)+sin(2*x)", 0, 8, 0.1));

        CalculatingGraphs.buildingDiscreteGraph(discreteChart, "y(x)=cos(x)+sin(2*x)", 0, 8, 0.5);

        quantizedChart.getData().add(CalculatingGraphs.getAnalogDataSeries("y(x)=cos(x)+sin(2*x)", 0, 8, 0.1));
        //CalculatingGraphs.buildingQuantizedGraph(quantizedChart, "y(x)=cos(x-0.1)+sin(2*x-0.1)",0, 8, 0.1);
        CalculatingGraphs.buildingQuantizedGraph(quantizedChart, "y(x)=cos(x-0.25)+sin((2*x)-0.25)", 0, 8, 0.25);

        CalculatingGraphs.buildingFFTGraph(fftChart, "y(x)=cos(x)+sin(2*x)", 0, 8, 0.01);*/

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
}