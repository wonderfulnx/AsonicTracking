package com.example.asonictrackerreceiver;

public class Config {
    public static final int SamplingRate = 44100;
    public static final int StartFreq = 16000;
    public static final int EndFreq = 18000;
    public static final double T = 0.04;
    public static final int BandPassCenter = 17000;
    public static final int BandPassOffset = 2000;

    public static final int SampleNum = 1 + (int)(T * SamplingRate);
}
