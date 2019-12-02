package com.example.asonictrackerreceiver;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TrackView trackView;
    private Random r = new Random();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trackView = findViewById(R.id.trackView);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.appendData();
            }
        });
    }

    private void appendData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.trackView.drawPath(r.nextFloat() * 200, r.nextFloat() * 200);
            }
        });
    }
}


//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.ContactsContract;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.widget.Button;
//import android.graphics.Color;
//
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;
//import com.jjoe64.graphview.series.PointsGraphSeries;
//
//
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//
//import java.util.Map;
//import java.util.Random;
//import java.util.List;
//import java.util.ArrayList;
//import android.util.Log;
//import android.widget.TextView;
//
//public class MainActivity extends AppCompatActivity {
//
//    private Button btn;
////    private int x = 0;
//    private Random r = new Random();
//////    private final LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {new DataPoint(0, 0.5)});
////    private PointsGraphSeries<DataPoint> series;
////    private LineChart chart;
//
////    private TrackView trackView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        trackView = findViewById(R.id.trackView);
//
//        setContentView(R.layout.activity_main);
//
////        btn = findViewById(R.id.button);
////        btn.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View v) {
////
////            }
////        });
//
////        chart = (LineChart) findViewById(R.id.chart);
////
////        List<Entry> entries = new ArrayList<Entry>();
////        entries.add(new Entry(2, 0));
////        entries.add(new Entry(0, 2));
////        entries.add(new Entry(0, 0));
////        entries.add(new Entry(2, 2));
////
////        LineDataSet dataSet = new LineDataSet(entries, "Label");
////        dataSet.setColor(ColorTemplate.getHoloBlue());
////        dataSet.setCircleColor(Color.WHITE);
////        dataSet.setLineWidth(2f);
////        dataSet.setCircleRadius(4f);
////        dataSet.setFillAlpha(65);
////        dataSet.setFillColor(ColorTemplate.getHoloBlue());
////        dataSet.setHighLightColor(Color.rgb(244, 117, 117));
////        dataSet.setValueTextColor(Color.WHITE);
////        dataSet.setValueTextSize(9f);
////        dataSet.setDrawValues(false);
////        LineData lineData = new LineData(dataSet);
////        chart.setData(lineData);
////        chart.invalidate();
//
//
////        series = new PointsGraphSeries<>();
////        GraphView graph = (GraphView) findViewById(R.id.graph);
////        graph.addSeries(series);
////        graph.getViewport().setXAxisBoundsManual(true);
////        graph.getViewport().setMinX(0);
////        graph.getViewport().setMaxX(40);
////
////        btn = findViewById(R.id.button);
////        btn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                MainActivity.this.appendData(r.nextDouble(), r.nextDouble());
////            }
////        });
//    }

//    private void appendData(final double x, final double y) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("add point", String.format("(%f, %f)", x, y));
//                series.appendData(new DataPoint(x, y), true, 40);
//            }
//        });
//    }

//    private void addEntry(float x, float y) {
//        Log.i("here", String.format("x:%f, y:%f", x, y));
//        LineData data = chart.getData();
//
//        if (data != null) {
//            ILineDataSet set = data.getDataSetByIndex(0);
//            data.addEntry(new Entry(x, y), 0);
//            data.notifyDataChanged();
//            chart.notifyDataSetChanged();
//            chart.setVisibleXRangeMaximum(120);
////            chart.moveViewToX(data.getEntryCount());
//        }
//    }
//    private void appendData() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                MainActivity.this.trackView.drawPath(r.nextFloat(), r.nextFloat());
//            }
//        });
//    }
//}
