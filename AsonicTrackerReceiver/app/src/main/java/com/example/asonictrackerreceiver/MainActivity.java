package com.example.asonictrackerreceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements CallBack {
    protected static final String TAG = "MainActivity";
    private Button start_btn;
    private EditText editText;
    private Recorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();

        // editText init
        this.editText = this.findViewById(R.id.editText);
        this.editText.setKeyListener(null);

        // start btn init
        this.start_btn = this.findViewById(R.id.start_btn);
        this.start_btn.setOnClickListener(new View.OnClickListener() {
            boolean recording = false;
            @Override
            public void onClick(View view) {
                if (this.recording) {
                    this.recording = false;

                    // Stop Recording
                    MainActivity.this.recorder.recording = false;
                    try {
                        MainActivity.this.recorder.join();
                    } catch (InterruptedException e){
                        Log.e(TAG, "record thread Interrupted;");
                    }
                    MainActivity.this.start_btn.setText("Start");
                }
                else {
                    this.recording = true;

                    // Start Recording
                    recorder = new Recorder(MainActivity.this);
                    recorder.start();

                    MainActivity.this.start_btn.setText("Stop");
                }
            }
        });
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

    @Override
    public void solve_distance(double dis) {
        // 计算结果结束后进行的callback
        logToDisplay(String.format("Distance is: %.3f m", dis));
    }

    @Override
    public void solve_position(double x, double y) {
        logToDisplay(String.format("Position is: (%.3f, %3f) m", x, y));
    }

    private void logToDisplay(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.editText.append(msg + "\n");
//                MainActivity.this.editText.setText(msg);
            }
        });
    }
}
