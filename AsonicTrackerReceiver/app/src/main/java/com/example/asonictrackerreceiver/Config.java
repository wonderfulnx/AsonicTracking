package com.example.asonictrackerreceiver;

public class Config {
    public static final int SamplingRate = 44100;
    public static final int StartFreqA = 17000;
    public static final int EndFreqA = 19000;
    public static final int StartFreqB = 10000;
    public static final int EndFreqB = 14000;
    public static final double T = 0.04;
    public static final int BandPassCenterA = 18000;
    public static final int BandPassCenterB = 12000;
    public static final int BandPassOffsetA = 2000;
    public static final int BandPassOffsetB = 3000;
    public static final int StartThreshold = 50;
    public static final int SoundSpeed = 340;
    public static final int FMCW_FFTLen = 1024 * 64;
    public static final double SpeakerDist = 0.5;

    public static final int SampleNum = 1 + (int)(T * SamplingRate);
}
