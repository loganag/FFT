package com.example.FFT;

public class FFT {

    public static Complex[] fft(Complex[] a)
    {
        int n = a.length;

        if(n == 1 ) return a;

        //if((n & (n - 1)) == 0) return new Exception("Не является степенью 2-ки");

        //Расчет для четной части
        Complex[] even = new Complex[n/2];
        for(int i = 0; i < n/2; i++){
            even[i] = a[2 * i];
        }
        Complex[] evenFFT = fft(even);
        //show(evenFFT);

        //Расчет для нечетной части
        Complex[] odd = new Complex[n/2];
        for(int i = 0; i < n/2; i++)
        {
            odd[i] = a[2 * i +1];
        }
        Complex[] oddFFT = fft(odd);
        //show(evenFFT);

        //Объединение результатов
        Complex[] y = new Complex[n];
        for(int i = 0; i<n/2; i++)
        {
            double Wi = (-2 * i * Math.PI) / n;
            Complex complexWi = new Complex(Math.cos(Wi), Math.sin(Wi));
            y[i] = evenFFT[i].plus(complexWi.multiply(oddFFT[i]));
            y[i + n/2] = evenFFT[i].minus(complexWi.multiply(oddFFT[i]));

        }

        return y;
    }

    public static void show(Complex[] x)
    {
        for(int i =0; i< x.length; i++)
        {
            System.out.println(x[i]);
        }
    }

}

