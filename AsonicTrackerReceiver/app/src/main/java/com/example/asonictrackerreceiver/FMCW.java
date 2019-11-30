package com.example.asonictrackerreceiver;

public class FMCW {
    private int fs;
    private double T;
    private int f0;
    private int f1;
    private int sample_num;
    private double[] pseudo_T;
    private int fft_len = Config.FMCW_FFTLen;
    private int c = Config.SoundSpeed;

    public FMCW(int sample_freq, double chirp_T, int start_freq, int end_freq) {
        fs = sample_freq;
        T = chirp_T;
        f0 = start_freq;
        f1 = end_freq;
        sample_num = 1 + (int)(T * fs);
        double[] t = new double[sample_num];
        for (int i = 0; i < sample_num; i++) t[i] = i * ((double)1 / fs);
        pseudo_T = Utils.chirp(t, f0, T, f1);
    }

    public double delta_dis(double[] input) {
        if (input.length != sample_num)
            throw new RuntimeException("input data length wrong!");

        // 点乘 计算FFT
        Complex[] s = new Complex[fft_len];
        for (int i = 0; i < fft_len && i < sample_num; i++)
            s[i] = new Complex(pseudo_T[i] * input[i], 0);
        for (int i = sample_num; i < fft_len; i++)
            s[i] = new Complex(0, 0);

        return cal_delta(s);
    }

    public double delta_dis(double[] input, int start) {
        if (input.length - start < sample_num)
            throw new RuntimeException("input data length wrong!");

        Complex[] s = new Complex[fft_len];
        for (int i = 0; i < fft_len && i < sample_num; i++)
            s[i] = new Complex(pseudo_T[i] * input[start + i], 0);
        for (int i = sample_num; i < fft_len; i++)
            s[i] = new Complex(0, 0);

        return cal_delta(s);
    }

    private double cal_delta(Complex[] s) {
        // 计算频率偏移
        Complex[] FFT_out = FFT.fft(s);
        double max_delta = 0;
        int max_ind = -1;
        for (int i = 0; i < fft_len / 2; i++){
            double delta = FFT_out[i].abs();
            if (delta > max_delta) {
                max_delta = delta;
                max_ind = i;
            }
        }
        return max_ind * fs * c * T / (f1 - f0) / fft_len;
    }
}
