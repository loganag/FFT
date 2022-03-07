package com.example.FFT;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import org.mariuszgromada.math.mxparser.Function;

public class CalculatingGraphs {

    public static XYChart.Series getAnalogDataSeries(String function, double initValue, double finalValue, double sampling){
        double y;
        Function f1 = new Function(function);

        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName(function);

        double x = initValue;
        while (!(x>=finalValue))
        {
            y = f1.calculate(x);
            dataSeries.getData().add(new XYChart.Data<>(x, y));
            x += sampling;
        }

        return dataSeries;
    }

    public static void buildingDiscreteGraph(LineChart<Number, Number> discreteChart, String function, double initValue, double finalValue, double sampling)
    {
        double y;
        Function f1 = new Function(function);

        Color color = Color.BLUE;

        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        double x = initValue;
        while (!(x>=finalValue))
        {
            y = f1.calculate(x);

            XYChart.Series dataSeries = new XYChart.Series();
            dataSeries.getData().add(new XYChart.Data<>(x, y));
            dataSeries.getData().add(new XYChart.Data<>(x, 0));
            discreteChart.getData().add(dataSeries);

            Node line = dataSeries.getNode().lookup(".chart-series-line");
            line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");

            x += sampling;
        }



    }
}
