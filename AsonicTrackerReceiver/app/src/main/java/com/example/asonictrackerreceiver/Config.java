package com.example.asonictrackerreceiver;

public class Config {
    public static final int SamplingRate = 48000;
    public static final int StartFreq = 17000;
    public static final int EndFreq = 19000;
    public static final double T = 0.04;
    public static final int BandPassCenter = 18000;
    public static final int BandPassOffset = 2000;
    public static final int StartThreshold = 50;
    public static final int SoundSpeed = 340;
    public static final int FMCW_FFTLen = 1024 * 64;

    public static final int SampleNum = 1 + (int)(T * SamplingRate);
}
