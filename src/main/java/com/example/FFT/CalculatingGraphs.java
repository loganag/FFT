package com.example.FFT;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import org.mariuszgromada.math.mxparser.Function;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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

    public static XYChart.Series getAnalogDataSeries(Complex[] array, int n)
    {
        XYChart.Series dataSeries = new XYChart.Series();

        for (int i = 0; i < n; i++) {
            dataSeries.getData().add(new XYChart.Data<>(array[i].re(), array[i].im()));
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

    public static void buildingDiscreteGraph(LineChart<Number, Number> discreteChart, Complex[] array, int n)
    {
        Color color = Color.BLUE;

        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        for (int i = 0; i < n; i++) {
            XYChart.Series dataSeries = new XYChart.Series();
            dataSeries.getData().add(new XYChart.Data<>(array[i].re(), array[i].im()));
            dataSeries.getData().add(new XYChart.Data<>(array[i].re(), 0.0));
            discreteChart.getData().add(dataSeries);

            Node line = dataSeries.getNode().lookup(".chart-series-line");
            line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
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


    public static void buildingFFTGraph(LineChart<Number, Number> fftChart, String function, double initValue, double finalValue, double sampling)
    {
        int oldN = (int) Math.floor((finalValue-initValue)/sampling) + 1;

        int n = (int) Math.pow(2, Math.ceil(Math.log(oldN)/Math.log(2)));     //???????????????????? ?? ?????????????? 2-????

        double frequencyStep = 1/((finalValue-initValue)*sampling);
        double frequencyCoefficient = oldN * 1.0 / n;
        frequencyStep *= frequencyCoefficient;

        double[] frequency = new double[n];
        double[] magnitude = new double[n];

        XYChart.Series dataSeries = new XYChart.Series();

        int j=0;
        for (int i = -(n/2); i < (n/2); i++)
        {
            try {
                frequency[j] = i * frequencyStep;
                //System.out.println(frequency[j]);
                j++;
            }
            catch (ArrayIndexOutOfBoundsException e){
                break;
            }
        }

        Complex[] initialData = FFT.vectorPreparation(function, initValue, finalValue, sampling);
        Complex[] fftResult = FFT.fft(initialData);

        //???????????????????? ?????????????????? ???????????????????????? ??????????????
        for (int i = 0; i < n; i++)
        {
            magnitude[i] = fftResult[i].abs();
            //System.out.println("fq" + frequency[i] + " mg= " + magnitude[i]);
        }

        //???????????????????????????? ?????????????? ?????????? ??????????
        double[] invertedMagnitude = new double[n];
        j = 0;
        for(int i = n/2; i > 0; i--)
        {
            invertedMagnitude[i] = magnitude[j];
            j++;
        }

        j = n-1;
        for(int i = n/2; i < n; i++){
            invertedMagnitude[i] = magnitude[j];
            j--;
        }

        for (int i = 0; i < n; i++)
        {
            dataSeries.getData().add(new XYChart.Data<>(frequency[i], invertedMagnitude[i]));
        }

        fftChart.getData().add(dataSeries);
    }

    public static void buildingFFTGraph(LineChart<Number, Number> fftChart, Complex[] points, int n, int oldN)
    {
        double sampling = (points[1].re() - points[0].re());
        System.out.println(sampling);
        //double frequencyStep = 1/((points[oldN-1].re()-points[0].re())*sampling);
        double frequencyCoefficient = oldN * 1.0 / n;
        double frequencyStep = 1/(points[oldN-1].re() - points[0].re());
        frequencyStep *= frequencyCoefficient;
        System.out.println(frequencyStep);
        double[] frequency = new double[n];
        double[] magnitude = new double[n];

        XYChart.Series dataSeries = new XYChart.Series();

        int j=0;
        for (int i = -(n/2); i < (n/2); i++)
        {
            try {
                frequency[j] = i * frequencyStep;
                j++;
            }
            catch (ArrayIndexOutOfBoundsException e){
                break;
            }
        }

        Complex[] fftResult = FFT.fft(points);


        try {
            PrintWriter writer = new PrintWriter("combinationFFT.txt", StandardCharsets.UTF_8);
            for (Complex item:fftResult) {
                writer.println(Double.toString(item.re()) + "\t" +Double.toString(item.im()));
                System.out.println(item.re() + "\t" + item.im());
            }
            writer.close();

        }
        catch (IOException e)
        {
            System.out.println("Can't write to file");
        }

        double max = -1000000;
        int maxI=0;
        //???????????????????? ?????????????????? ???????????????????????? ??????????????
        for (int i = 0; i < n; i++)
        {
            magnitude[i] = fftResult[i].abs();
            //System.out.println("fq" + frequency[i] + " mg= " + magnitude[i]);
            if(magnitude[i] > max) {
                max = magnitude[i];
                maxI = i;
            }
        }

        System.out.println("Maximum " + max);
        System.out.println("frequency " + frequency[maxI]);


        //???????????????????????????? ?????????????? ?????????? ??????????
        double[] invertedMagnitude = new double[n];
        j = 0;
        for(int i = n/2; i > 0; i--)
        {
            invertedMagnitude[i] = magnitude[j];
            j++;
        }

        j = n-1;
        for(int i = n/2; i < n; i++){
            invertedMagnitude[i] = magnitude[j];
            j--;
        }


        for (int i = 0; i <n; i++) {
            System.out.println(invertedMagnitude[i]);
        }



        for (int i = 0; i < n; i++)
        {
            dataSeries.getData().add(new XYChart.Data<>(frequency[i], invertedMagnitude[i]));
        }

        fftChart.getData().add(dataSeries);
    }
}
