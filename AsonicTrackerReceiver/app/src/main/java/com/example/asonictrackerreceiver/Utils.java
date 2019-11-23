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
