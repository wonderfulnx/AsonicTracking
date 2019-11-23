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
            for (int j = 0; j < target.length && j < input.length - i; ++j) {
                xcorr_result[i] += input[i + j] * target[j];
            }
        }
        return xcorr_result;
    }

    public static int findStart(double[] input) {
        int sample_num = 1 + (int)(Config.T * Config.SamplingRate);
        double[] t = new double[sample_num];
        for (int i = 0; i < sample_num; i++) t[i] = i * ((double)1 / Config.SamplingRate);

        double[] xcorr_result = xcorr(input, Utils.chirp(t, Config.StartFreq, Config.T, Config.EndFreq));
        for (int i = 0; i < xcorr_result.length; ++i) {
            if (xcorr_result[i] > 10) {
                return i;
            }
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
