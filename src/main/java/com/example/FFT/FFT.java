package com.example.FFT;

import org.mariuszgromada.math.mxparser.Function;

public class FFT {
    public static Complex[] fft(Complex[] a)
    {
        int n = a.length;

        if(n == 1 ) return new Complex[] { a[0] };

        //Расчет для четной части
        Complex[] even = new Complex[n/2];
        for(int i = 0; i < n/2; i++){
            even[i] = a[2 * i];
        }
        Complex[] evenFFT = fft(even);

        //Расчет для нечетной части
        Complex[] odd = new Complex[n/2];
        for(int i = 0; i < n/2; i++)
        {
            odd[i] = a[2 * i +1];
        }
        Complex[] oddFFT = fft(odd);

        //show(evenFFT);
        //System.out.println("\n");
        //show(oddFFT);

        //Объединение результатов
        Complex[] y = new Complex[n];
        for(int i = 0; i<n/2; i++)
        {
            double Wi = (-2 * i * Math.PI) / n;
            Complex complexWi = new Complex(Math.cos(Wi), Math.sin(Wi));
            y[i] = evenFFT[i].plus(complexWi.multiply(oddFFT[i]));
            y[i + (n/2)] = evenFFT[i].minus(complexWi.multiply(oddFFT[i]));

        }
        //System.out.printf("Rez= ");
        //show(y);
        return y;
    }

    public static void show(Complex[] x)
    {
        for(int i =0; i< x.length; i++)
        {
            //System.out.println(x[i]);
            System.out.printf(x[i] + " ");
        }
    }

    public static Complex[] vectorPreparation(String function, double initValue, double finalValue, double sampling)
    {
        Function f1 = new Function(function);
        double x = initValue;
        int n = (int) Math.floor((finalValue-initValue)/sampling) + 1;

        int newN = (int) Math.pow(2, Math.ceil(Math.log(n)/Math.log(2)));     //Приведение к степени 2-ки


        Complex[] arr = new Complex[newN];
        for (int i = 0; i < newN; i++) {
            arr[i] = new Complex(f1.calculate(x), 0);
            x += sampling;
        }
        return arr;
    }

}

