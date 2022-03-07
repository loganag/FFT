package com.example.FFT;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public static void buildingQuantizedGraph(LineChart<Number, Number> quantizedChart, String function, double initValue, double finalValue, double sampling)
    {
        double y;
        double y1;
        int sign;
        int n = (int) Math.floor((finalValue-initValue)/sampling) + 1;
        //List<ArrayList<Light.Point>> points = new ArrayList<>();
        ArrayList<Double> points = new ArrayList<>();
        Function f1 = new Function(function);

        XYChart.Series dataSeries = new XYChart.Series();

        double x = initValue;
        while(!(x>=finalValue))
        {

            y = f1.calculate(x);
            sign = (int) Math.signum(y);
            //System.out.println(sign);
            y1 = (Math.abs(y)%sampling);
            if(y1 == 0)
                points.add(y);
            else
            {
                y1 = Math.abs(y) + (sampling-y1);
                points.add(sign * y1);
            }
            x+=sampling;
        }

        x = initValue;
        dataSeries.getData().add(new XYChart.Data<>(x, points.get(0)));

        for (int i = 1; i < points.size()-1; i++) {
            if (points.get(i + 1) > points.get(i)) {
                dataSeries.getData().add(new XYChart.Data<>(x, points.get(i)));
                dataSeries.getData().add(new XYChart.Data<>(x + sampling, points.get(i)));
                System.out.println("x= " + x + "Y= " + points.get(i));
                x += sampling;
            } else if (points.get(i + 1) < points.get(i)) {
                if (points.get(i) > 0) {
                    dataSeries.getData().add(new XYChart.Data<>(x + sampling, points.get(i)));
                    dataSeries.getData().add(new XYChart.Data<>(x, points.get(i)));
                } else {
                    dataSeries.getData().add(new XYChart.Data<>(x + sampling, points.get(i)));
                    dataSeries.getData().add(new XYChart.Data<>(x, points.get(i)));
                }
                System.out.println("x= " + x + "Y= " + points.get(i));
                x += sampling;
            } else {
                dataSeries.getData().add(new XYChart.Data<>(x, points.get(i)));
                x += sampling;
            }
        }

        quantizedChart.getData().add(dataSeries);

    }
}
