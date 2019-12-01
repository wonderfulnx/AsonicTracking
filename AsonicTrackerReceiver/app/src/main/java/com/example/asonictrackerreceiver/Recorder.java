package com.example.asonictrackerreceiver;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.concurrent.ConcurrentNavigableMap;

interface CallBack {
    void solve_distance(double dis);
    void solve_position(double x, double y);
}

public class Recorder extends Thread {
    protected static final String TAG = "Recorder";
    private int SamplingRate = Config.SamplingRate;
    private int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord audioRecord;
    private int bufferSize;
    private byte[] buffer;
    private BandPassFilter bandPassFilterA;
    private FMCW fmcwA;
    private BandPassFilter bandPassFilterB;
    private FMCW fmcwB;
    CallBack callBack;

    public boolean recording = false;

    public Recorder(CallBack call_back) {
        super();
        bufferSize = AudioRecord.getMinBufferSize(SamplingRate, channelConfiguration, audioEncoding);
        bufferSize *= 3;
        // Log.i(TAG, "buffer size is: " + bufferSize);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SamplingRate,
                channelConfiguration, audioEncoding, bufferSize * 50);
        buffer = new byte[bufferSize];
        bandPassFilterA = new BandPassFilter(Config.BandPassCenterA, Config.BandPassOffset, Config.SamplingRate);
        fmcwA = new FMCW(Config.SamplingRate, Config.T, Config.StartFreqA, Config.EndFreqA);
        bandPassFilterB = new BandPassFilter(Config.BandPassCenterB, Config.BandPassOffset, Config.SamplingRate);
        fmcwB = new FMCW(Config.SamplingRate, Config.T, Config.StartFreqB, Config.EndFreqB);
        callBack = call_back;
    }

    @Override
    public void run() {
        try {
            // Main work for recording
            recording = true;
            audioRecord.startRecording();
            int last_tail_a = 0;
            int last_tail_b = 0;
            boolean aligned_a = false;
            boolean aligned_b = false;
            int read_cnt = 0;
            while (recording) {
                int num_rec = audioRecord.read(buffer, 0, bufferSize);
                if (num_rec == AudioRecord.ERROR_INVALID_OPERATION || num_rec == AudioRecord.ERROR_BAD_VALUE)
                    continue;
                if (num_rec != 0 && num_rec != -1) {
                    // do signal process here
                    double[] audio = Utils.bytes2double(buffer, num_rec);
                    double[] receive_a = bandPassFilterA.filter(audio);
                    double[] receive_b = bandPassFilterB.filter(audio);
                    int start_pos_a = -1;
                    int start_pos_b = -1;
//                    if (aligned) start_pos = 2 * Config.SampleNum - last_tail;
//                    else {
//                        start_pos = Utils.findStart(receive);
//                        if (start_pos >= 0) aligned = true;
//                    }
                    if (aligned_a && aligned_b) {
                        start_pos_a = 2 * Config.SampleNum - last_tail_a;
                        start_pos_b = 2 * Config.SampleNum - last_tail_b;
                    } else if (aligned_a) {
                        start_pos_b = Utils.findStart(receive_b);
                        if (start_pos_b >= 0) aligned_b = true;
                        if (start_pos_a >= 0) {
                            while (start_pos_a + Config.SampleNum * 2 <= receive_a.length) {
                                start_pos_a += Config.SampleNum * 2;
                            }
                            last_tail_a = receive_a.length - start_pos_a;
                        }
                    } else if (aligned_b) {
                        start_pos_a = Utils.findStart(receive_a);
                        if (start_pos_a >= 0) aligned_a = true;
                        if (start_pos_b >= 0) {
                            while (start_pos_b + Config.SampleNum * 2 <= receive_b.length) {
                                start_pos_b += Config.SampleNum * 2;
                            }
                            last_tail_b = receive_b.length - start_pos_b;
                        }
                    } else {
                        start_pos_a = Utils.findStart(receive_a);
                        if (start_pos_a >= 0) aligned_a = true;
                        start_pos_b = Utils.findStart(receive_b);
                        if (start_pos_b >= 0) aligned_b = true;
                    }

                    if (start_pos_a >= 0 && start_pos_b >= 0) {
                        while (start_pos_a + Config.SampleNum * 2 <= receive_a.length && start_pos_b + Config.SampleNum * 2 <= receive_b.length) {
                            double dis_a = fmcwA.delta_dis(receive_a, start_pos_a);
                            double dis_b = fmcwB.delta_dis(receive_b, start_pos_b);
                            double x = (Config.SpeakerDist * Config.SpeakerDist + dis_a * dis_a - dis_b * dis_b) / (2 * Config.SpeakerDist);
                            double y = Math.sqrt(dis_a * dis_a - x * x);

                            callBack.solve_position(x, y);

                            start_pos_a += Config.SampleNum * 2;
                            start_pos_b += Config.SampleNum * 2;
                        }

                        while (start_pos_a + Config.SampleNum * 2 <= receive_a.length) {
                            start_pos_a += Config.SampleNum * 2;
                        }

                        while (start_pos_b + Config.SampleNum * 2 <= receive_b.length) {
                            start_pos_b += Config.SampleNum * 2;
                        }

                        last_tail_a = receive_a.length - start_pos_a;
                        last_tail_b = receive_b.length - start_pos_b;
                        read_cnt++;
                    }

//                    if (start_pos >= 0) {
//                        // Log.i("START_POS", String.format("last tail is: %d, new start is: %d", last_tail, start_pos));
//                        // boolean caled = false;
//                        while (start_pos + Config.SampleNum * 2 <= receive.length) {
//                            // if (!caled) {
//                            double dis = fmcwA.delta_dis(receive, start_pos);
//                            callBack.solve_distance(dis);
//                            // caled = true;
//                            //}
//                            start_pos += Config.SampleNum * 2;
//                        }
//                        last_tail = receive.length - start_pos;
//                        read_cnt++;
//                    }
                }
            }
            audioRecord.stop();
        } catch (Throwable t){
            Log.e(TAG, "failed to record");
        }

    }

}
