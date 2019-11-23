package com.example.asonictrackerreceiver;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
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

    public Recorder() {
        super();
        bufferSize = AudioRecord.getMinBufferSize(SamplingRate, channelConfiguration, audioEncoding);
        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SamplingRate,
                channelConfiguration, audioEncoding, bufferSize * 2);
        buffer = new byte[bufferSize];
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

                    // process the received signal here
                    callBack.solve_distance(0.);
                }
            }
            audioRecord.stop();
        } catch (Throwable t){
            Log.e(TAG, "failed to record");
        }

    }

}
