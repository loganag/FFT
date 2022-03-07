package com.example.FFT;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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

        analogChart.getData().add(CalculatingGraphs.getAnalogDataSeries("y(x)=cos(x)+sin(2*x)",0, 8, 0.1));

        CalculatingGraphs.buildingDiscreteGraph(discreteChart, "y(x)=cos(x)+sin(2*x)",0, 8, 0.5);

        quantizedChart.getData().add(CalculatingGraphs.getAnalogDataSeries("y(x)=cos(x)+sin(2*x)",0, 16, 0.1));
        CalculatingGraphs.buildingQuantizedGraph(quantizedChart, "y(x)=cos(x)+sin(2*x)",0, 16, 0.2);
    }
}