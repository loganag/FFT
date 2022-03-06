package com.example.FFT;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button buildButton;

    @FXML
    private LineChart<Number, Number> analogChart;

    @FXML
    private LineChart<Number, Number> discreteChart;

    @FXML
    private LineChart<Number, Number> quantizedChart;

    @FXML
    private LineChart<Number, Number> fftChart;

    @FXML
    void buildButtonClicked(MouseEvent event) {
        buildButton.setText("Нажато");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Axis<Number> xAxisAnalog = analogChart.getXAxis();
        Axis<Number> yAxisAnalog = analogChart.getYAxis();
        xAxisAnalog.setLabel("Время (сек)");
        yAxisAnalog.setLabel("Амплитуда");

        Axis<Number> xAxisDiscrete = discreteChart.getXAxis();
        Axis<Number> yAxisDiscrete = discreteChart.getYAxis();
        xAxisDiscrete.setLabel("Время (сек)");
        yAxisDiscrete.setLabel("Амплитуда");

        Axis<Number> xAxisQuantized = quantizedChart.getXAxis();
        Axis<Number> yAxisQuantized = quantizedChart.getYAxis();
        xAxisQuantized.setLabel("Время (сек)");
        yAxisQuantized.setLabel("Амплитуда");

        Axis<Number> xAxisFFT = fftChart.getXAxis();
        Axis<Number> yAxisFFT = fftChart.getYAxis();
        xAxisFFT.setLabel("Частота (Гц)");
        yAxisFFT.setLabel("Магнитуда");
    }
}