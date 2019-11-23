package com.example.asonictrackerreceiver;

public class Utils {
    public static double[] chirp(double[] t, int f0, double t1, int f1) {
        double t0 = t[0];
        int len = t.length;
        double k = (f1 - f0) / (t1 - t0);
        double[] signal = new double[len];
        for (int i = 0; i < len; i++) {
            signal[i] = Math.cos(2 * Math.PI * (k / 2 * t[i] + f0) * t[i]);
        }
        return signal;
    }

    public static double[] xcorr(double[] input, double[] target) {
        double[] xcorr_result = new double[input.length];
        for (int i = 0; i < input.length; ++i) {
            xcorr_result[i] = 0;
            for (int j = 0; j < target.length; ++j) {
                xcorr_result[i] += input[i + j] * target[j];
            }
        }
        return xcorr_result;
    }

    public static int findStart(double[] input, double[] target) {
        double[] xcorr_result = xcorr(input, target);
        for (int i = 0; i < xcorr_result.length; ++i) {
            if (xcorr_result[i] > 10) {
                return i;
            }
        }
        return -1;
    }
}
