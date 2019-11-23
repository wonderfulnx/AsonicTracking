package com.example.asonictrackerreceiver;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telecom.Call;
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
    CallBack callBack;

    public boolean recording = false;

    public Recorder(CallBack call_back) {
        super();
        bufferSize = AudioRecord.getMinBufferSize(SamplingRate, channelConfiguration, audioEncoding);
//        bufferSize *= 10;
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SamplingRate,
                channelConfiguration, audioEncoding, bufferSize * 2);
        buffer = new byte[bufferSize];
        callBack = call_back;
    }

    @Override
    public void run() {
        try {
            // Main work for recording
            recording = true;
            audioRecord.startRecording();
            while (recording) {
                int num_rec = audioRecord.read(buffer, 0, bufferSize);
                if (num_rec == AudioRecord.ERROR_INVALID_OPERATION || num_rec == AudioRecord.ERROR_BAD_VALUE)
                    continue;
                if (num_rec != 0 && num_rec != -1) {
                    double[] receive = Utils.bytes2double(buffer, num_rec);
                    BandPassFilter bpass = new BandPassFilter(20000, 3000, Config.SamplingRate);
                    receive = bpass.filter(receive);

                    // process the received signal here
                    int start_pos = Utils.findStart(receive);
                    if (start_pos >= 0) {
                        FMCW fmcw = new FMCW(Config.SamplingRate, Config.T, Config.StartFreq, Config.EndFreq);
                        while (start_pos + Config.SampleNum < receive.length) {
                            double dis = fmcw.delta_dis(receive, start_pos);
                            callBack.solve_distance(dis);
                            start_pos += Config.SampleNum;
                        }
                    }
                    else callBack.solve_distance(0.);
                }
            }
            audioRecord.stop();
        } catch (Throwable t){
            Log.e(TAG, "failed to record");
        }

    }

}
