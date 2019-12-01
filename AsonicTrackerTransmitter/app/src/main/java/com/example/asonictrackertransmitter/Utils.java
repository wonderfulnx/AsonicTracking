package com.example.asonictrackertransmitter;

import android.os.Environment;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {
    public static String messageFilePath = Environment.getExternalStorageDirectory() + "/AsonicTracker/message.wav";

    public static double[] chirp(double[] t, int f0, double t1, int f1) {
        double t0 = t[0];
        int len = t.length;
        double k = (f1 - f0) / (t1 - t0);
        double[] signal = new double[len];
        for (int i = 0; i < len; i++) {
            signal[i] = Math.cos(2 * Math.PI * (k / 2 * t[i] + f0) * t[i]);
        }
        return signal;
    }

    public static byte[] doubles2bytes(double[] ds) {
        byte[] ans = new byte[2 * ds.length];
        int idx = 0;
        for (final double dval: ds) {
            final short val = (short)(dval * 32767);
            ans[idx++] = (byte)(val & 0x00ff);
            ans[idx++] = (byte)((val & 0xff00) >>> 8);
        }
        return ans;
    }

    public static void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
                                     long totalDataLen, long longSampleRate, int channels, long byteRate)
            throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // WAV type format = 1
        header[21] = 0;
        header[22] = (byte) channels; //指示是单声道还是双声道
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff); //采样频率
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff); //每分钟录到的字节数
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff); //真实数据的长度
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        //把header写入wav文件
        out.write(header, 0, 44);
    }

    public static void writeMessage(double[] Tx_data, long SampleRate, String leftOrRight) {
        File file = new File(messageFilePath + leftOrRight);
        if (file.exists()) file.delete();
        try { file.createNewFile(); } catch (IOException e){
            throw new IllegalStateException("unable to create " + file.toString());
        }

        int channels = 1;
        //每分钟录到的数据的字节数
        long byteRate = 16 * SampleRate * channels / 8;

        byte[] tx_data = doubles2bytes(Tx_data);

        try {
            FileOutputStream os = new FileOutputStream(file);
            int audio_len = tx_data.length;
            WriteWaveFileHeader(os, audio_len, audio_len + 36, SampleRate, channels, byteRate);
            os.write(tx_data);
            os.close();
        } catch (Throwable t){
            Log.e("MainActivity", "failed to write");
        }
        return;
    }
}
