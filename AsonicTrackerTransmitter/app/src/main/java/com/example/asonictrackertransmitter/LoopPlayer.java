package com.example.asonictrackertransmitter;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class LoopPlayer extends Thread{

    private int fs;
    private double[] audio;
    private AudioTrack audioTrack;

    public LoopPlayer(int sample_freq, double[] signal) {
        fs = sample_freq;
        audio = signal;
    }

    @Override
    public void run() {
        super.run();
        byte[] tx = Utils.doubles2bytes(audio);
        int buff_size = tx.length;
        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                fs,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                buff_size,
                AudioTrack.MODE_STATIC);
        audioTrack.write(tx, 0, buff_size);
        // Log.i("LoopPlayer", "rua: " + audioTrack.getBufferSizeInFrames());
        int set_res = audioTrack.setLoopPoints(0, buff_size / 2, -1);
        // Log.i("LoopPlayer", "set loop point result: " + set_res);
        audioTrack.play();
    }

    public void stopRunning() {

        try {
            audioTrack.stop();
            audioTrack.release();
            this.join();
            // this.interrupt();
        }
        catch (InterruptedException e) {
            Log.e("LoopPlayer", " join operation interrupted");
        }
    }
}
