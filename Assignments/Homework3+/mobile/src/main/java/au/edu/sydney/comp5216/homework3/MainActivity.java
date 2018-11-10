package au.edu.sydney.comp5216.homework3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private long timestamp = 0;
    private TextView textViewStepCounter;
    private TextView textViewStepDetector;
    private Thread detectorTimeStampUpdaterThread;
    private Handler handler;
    private boolean isRunning = true;
    private long updateTime = System.currentTimeMillis();
    private int steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(au.edu.sydney.comp5216.homework3.R.layout.activity_main);

        textViewStepCounter = (TextView) findViewById(au.edu.sydney.comp5216.homework3.R.id.textView2);
        textViewStepDetector = (TextView) findViewById(au.edu.sydney.comp5216.homework3.R.id.textView4);

        registerForSensorEvents();
        setupDetectorTimestampUpdaterThread();
    }

    public void registerForSensorEvents() {
        SensorManager sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Step Counter
        sManager.registerListener(new SensorEventListener() {
            private int initialSteps = 0;


            @Override
            public void onSensorChanged(SensorEvent event) {
                if(initialSteps < 1){
                    initialSteps = (int) event.values[0];
                }

                steps = (int) event.values[0] - initialSteps;
                textViewStepCounter.setText(String.valueOf(steps));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_DELAY_UI); // SENSOR_DELAY_UI rate suitable for the user interface

        // Step Detector
        sManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                updateTime = System.currentTimeMillis();
//                if (timestamp == 0){
//                    timestamp = (event.timestamp) / 1000000;
//                } else{
                // Time is in nanoseconds, convert to millis
//                    updateTime = System.currentTimeMillis() + (event.timestamp) / 1000000 - timestamp;
//                    timestamp = (event.timestamp) / 1000000;
//                    Log.i("xxx", ""+ timestampDiff);
//                }


                Log.i("xxx", ""+timestamp);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR), SensorManager.SENSOR_DELAY_UI);
    }

    private void setupDetectorTimestampUpdaterThread() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = DateUtils.getRelativeTimeSpanString(updateTime, System.currentTimeMillis(),DateUtils.MINUTE_IN_MILLIS).toString();
                textViewStepDetector.setText(str);
                Log.i("xxx",""+str);
            }
        };

        // update "last step taken" every 5 seconds
        detectorTimeStampUpdaterThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(5000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        detectorTimeStampUpdaterThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        detectorTimeStampUpdaterThread.interrupt();
    }

    public void onReset(View view){
        steps = 0;
        textViewStepCounter.setText(String.valueOf(steps));
    }

}
