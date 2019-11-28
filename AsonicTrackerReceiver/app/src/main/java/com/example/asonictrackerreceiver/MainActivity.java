package com.example.asonictrackerreceiver;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;
import android.util.Log;
import android.widget.TextView;
//import com.example.asonictrackerreceiver.

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView textView;
    private int x = 0;
    private Random r = new Random();
//    private final LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {new DataPoint(0, 0.5)});
    private LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        series = new LineGraphSeries<>();
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);

        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x += 1;
                MainActivity.this.appendData(x, r.nextDouble());
            }
        });
    }

    private void appendData(final double x, final double y) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                series.appendData(new DataPoint(x, y), true, 40);
            }
        });
    }
}
