package com.example.asonictrackerreceiver;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

interface CallBack {
    void solve_distance(double dis);
}

public class Recorder extends Thread {
    protected static final String TAG = "Recorder";
    private int SamplingRate = Config.SamplingRate;
    private int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord audioRecord;
    private int bufferSize;
    private byte[] buffer;
    private BandPassFilter bandPassFilter;
    private FMCW fmcw;
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
        bandPassFilter = new BandPassFilter(Config.BandPassCenter, Config.BandPassOffset, Config.SamplingRate);
        fmcw = new FMCW(Config.SamplingRate, Config.T, Config.StartFreq, Config.EndFreq);
        callBack = call_back;
    }

    @Override
    public void run() {
        try {
            // Main work for recording
            recording = true;
            audioRecord.startRecording();
            int last_tail = 0;
            boolean aligned = false;
            int read_cnt = 0;
            while (recording) {
                int num_rec = audioRecord.read(buffer, 0, bufferSize);
                if (num_rec == AudioRecord.ERROR_INVALID_OPERATION || num_rec == AudioRecord.ERROR_BAD_VALUE)
                    continue;
                if (num_rec != 0 && num_rec != -1) {
                    // do signal process here
                    double[] audio = Utils.bytes2double(buffer, num_rec);
                    double[] receive = bandPassFilter.filter(audio);
                    int start_pos;
                    if (aligned) start_pos = 2 * Config.SampleNum - last_tail;
                    else {
                        start_pos = Utils.findStart(receive);
                        if (start_pos >= 0) aligned = true;
                    }

                    if (start_pos >= 0) {
                        // Log.i("START_POS", String.format("last tail is: %d, new start is: %d", last_tail, start_pos));
                        // boolean caled = false;
                        while (start_pos + Config.SampleNum * 2 <= receive.length) {
                            // if (!caled) {
                            double dis = fmcw.delta_dis(receive, start_pos);
                            callBack.solve_distance(dis);
                            // caled = true;
                            //}
                            start_pos += Config.SampleNum * 2;
                        }
                        last_tail = receive.length - start_pos;
                        read_cnt++;
                    }
                }
            }
            audioRecord.stop();
        } catch (Throwable t){
            Log.e(TAG, "failed to record");
        }

    }

}
