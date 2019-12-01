package com.example.asonictrackertransmitter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.io.IOError;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "PlayActivity";
    private Button play_btn;
    private Button draw_btn;
    private EditText fs_edit;
    private EditText f0_edit;
    private EditText f1_edit;
    private EditText T_edit;
    private EditText chirp_num_edit;
    private LoopPlayer loopPlayer_left;
    private LoopPlayer loopPlayer_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();

        // play btn
        this.play_btn = this.findViewById(R.id.play_btn);
        this.play_btn.setOnClickListener(new View.OnClickListener() {
            boolean playing = false;
            @Override
            public void onClick(View view) {
                if (playing) {
                    playing = false;

                    // Stop playing
                    try {
                        MainActivity.this.loopPlayer_left.stopRunning();
                        MainActivity.this.loopPlayer_right.stopRunning();
                    } catch (Exception e){
                        Log.e(TAG, "Error player: " + e.toString());
                    }

                    MainActivity.this.play_btn.setText("Play");
                }
                else {
                    try {
                        // get input
//                        int fs = Integer.parseInt(MainActivity.this.fs_edit.getText().toString());
//                        int f0 = Integer.parseInt(MainActivity.this.f0_edit.getText().toString());
//                        int f1 = Integer.parseInt(MainActivity.this.f1_edit.getText().toString());
//                        double T = Double.parseDouble(MainActivity.this.T_edit.getText().toString());
//                        int chirp_num = Integer.parseInt(MainActivity.this.chirp_num_edit.getText().toString());

                        int fs = 48000;
                        double T = 0.04;
                        int chirp_num = 10;
                        int f0_left = 17000;
                        int f1_left = 19000;
                        int f0_right = 14000;
                        int f1_right =16000;

                        // check input
//                        if (fs < 10000 || fs > 48000) throw new Exception("Wrong input");
//                        if (f0 > f1 || f1 > fs / 2) throw new Exception("Wrong input");
//                        if (T < 0.01 || T > 3) throw new Exception("Wrong input");
//                        if (chirp_num <= 0 || chirp_num > 100) throw new Exception("Wrong input");

                        // generate sound
                        int sample_num = 1 + (int)(T * fs);
                        double[] t = new double[sample_num];
                        for (int i = 0; i < sample_num; i++) t[i] = i * ((double)1 / fs);
//                        double[] chirp = Utils.chirp(t, f0, T, f1);

                        double[] chirp_left = Utils.chirp(t, f0_left, T, f1_left);
                        double[] message_left = new double[sample_num * 2 * chirp_num];
                        for (int i = 0; i < chirp_num; i++) {
                            for (int j = 0; j < sample_num; j++) message_left[i * 2 * sample_num + j] = chirp_left[j];
                            for (int j = sample_num; j < 2 * sample_num; j++) message_left[i * 2 * sample_num + j] = 0;
                        }
                        Utils.writeMessage(message_left, fs, "left");

                        double[] chirp_right = Utils.chirp(t, f0_right, T, f1_right);
                        double[] message_right = new double[sample_num * 2 * chirp_num];
                        for (int i = 0; i < chirp_num; i++) {
                            for (int j = 0; j < sample_num; j++) message_right[i * 2 * sample_num + j] = chirp_right[j];
                            for (int j = sample_num; j < 2 * sample_num; j++) message_right[i * 2 * sample_num + j] = 0;
                        }
                        Utils.writeMessage(message_right, fs, "right");

                        // Start playing
                        // MainActivity.this.mediaPlayer = new MediaPlayer();
                        // MainActivity.this.mediaPlayer.setDataSource(Utils.messageFilePath);
                        // MainActivity.this.mediaPlayer.prepare();
                        // MainActivity.this.mediaPlayer.setLooping(true);
                        // MainActivity.this.mediaPlayer.start();
                        loopPlayer_left = new LoopPlayer(fs, message_left);
                        loopPlayer_left.start();

                        loopPlayer_right = new LoopPlayer(fs, message_right);
                        loopPlayer_right.start();
                        playing = true;

                        MainActivity.this.play_btn.setText("Stop");
                    }
                    catch (Throwable e) {
                        Toast toast=Toast.makeText(MainActivity.this, "Failed to generate signal.\nPlease check input.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        // draw btn
        this.draw_btn = this.findViewById(R.id.draw_btn);

        // edit
        this.fs_edit = this.findViewById(R.id.fs_edit);
        this.f0_edit = this.findViewById(R.id.f0_edit);
        this.f1_edit = this.findViewById(R.id.f1_edit);
        this.T_edit = this.findViewById(R.id.T_edit);
        this.chirp_num_edit = this.findViewById(R.id.chirp_num_edit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!=
                PackageManager.PERMISSION_GRANTED||
        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }
}
