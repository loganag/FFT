package com.example.FFT;

import javafx.scene.chart.XYChart;
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
}
