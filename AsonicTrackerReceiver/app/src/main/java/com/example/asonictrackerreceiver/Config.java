package com.example.asonictrackerreceiver;

public class Config {
    public static final int SamplingRate = 48000;
    public static final int StartFreq = 18000;
    public static final int EndFreq = 20500;
    public static final double T = 0.04;

    public static final int SampleNum = 1 + (int)(T * SamplingRate);
}
