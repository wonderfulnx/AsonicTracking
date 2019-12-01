package com.example.asonictrackerreceiver;

import android.util.Log;

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
            for (int j = 0; j < target.length && j < input.length - i; ++j) {
                xcorr_result[i] += input[i + j] * target[j];
            }
        }
        return xcorr_result;
    }

//    public static int findStart(double[] input) {
//        double[] t = new double[Config.SampleNum];
//        for (int i = 0; i < Config.SampleNum; i++) t[i] = ((double)i / Config.SamplingRate);
//        double[] chirp = Utils.chirp(t, Config.StartFreq, Config.T, Config.EndFreq);
//
//        double[] xcorr_result = xcorr(input, chirp);
//
//        double max = 0;
//        for (double d: xcorr_result) if (d > max) max = d;
//
//        // Log.i("XCORR", String.format("max corr is: %.3f", max));
//
//        for (int i = 0; i < xcorr_result.length; ++i) {
//            if (xcorr_result[i] > Config.StartThreshold) return i;
//        }
//        return -1;
//    }
//
    public static int findStart(double[] input, int startFreq, int endFreq) {
        double[] t = new double[Config.SampleNum];
        for (int i = 0; i < Config.SampleNum; i++) t[i] = ((double)i / Config.SamplingRate);
        double[] chirp = Utils.chirp(t, startFreq, Config.T, endFreq);

        double[] xcorr_result = xcorr(input, chirp);

        double max = 0;
//        for (double d: xcorr_result) if (d > max) max = d;
//
//        // Log.i("XCORR", String.format("max corr is: %.3f", max));
//
//        for (int i = 0; i < xcorr_result.length; ++i) {
//            if (xcorr_result[i] > Config.StartThreshold) return i;
//        }

        int pos = -1;
        for (int i = 0; i < xcorr_result.length; ++i) {
            if (xcorr_result[i] > max) {
                max = xcorr_result[i];
                pos = i;
            }
        }
//        Log.i("XCORR", String.format("max corr is: %.3f, position is: %d", max, pos));
        if (max > Config.StartThreshold && pos > 20) {
            return pos - 20;
        }

        return -1;
    }

    public static double[] bytes2double(byte[] bytes, int length) {
        double[] doubles = new double[length / 2];
        for (int i = 0; i < doubles.length; i++) {
            byte bl = bytes[2 * i];
            byte bh = bytes[2 * i + 1];

            short s = (short) ((bh & 0x00FF) << 8 | bl & 0x00FF);
            doubles[i] = s / 32768f;
        }
        return doubles;
    }

    public static double[] bytes2double(byte[] bytes) {
        return bytes2double(bytes, bytes.length);
    }
}
