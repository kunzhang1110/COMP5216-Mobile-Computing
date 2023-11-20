package au.edu.sydney.comp5216.easydiet.log;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.joda.time.DateTime;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import au.edu.sydney.comp5216.easydiet.R;
import au.edu.sydney.comp5216.easydiet.food.FoodActivity;
import au.edu.sydney.comp5216.easydiet.profile.CalculatorActivity;
import au.edu.sydney.comp5216.easydiet.profile.User;
import au.edu.sydney.comp5216.easydiet.profile.UserActivity;

public class LogActivity extends AppCompatActivity {

    GraphView graph;
    Button btnLogSave;
    EditText etEnterWeight;
    //database fields
    LogEntryDB db;
    LogEntryDao dao;
    ArrayList<LogEntry> logEntryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final User user = (User) intent.getSerializableExtra("user");

        //Setup database
        db = LogEntryDB.getDatabase(this.getApplicationContext());
        dao = db.logEntryDao();

        setContentView(R.layout.activity_log);
        btnLogSave = (Button) findViewById(R.id.btnLogSave);
        etEnterWeight =(EditText) findViewById(R.id.etEnterWeight);
//        showTestCase();
        final BottomNavigationView navigationView =  (BottomNavigationView) findViewById(R.id.navigationView);

        logEntryList = new ArrayList<>();
        readItemsFromDatabase();
        Log.i("xxxList", ""+logEntryList);
        drawGraph();
        drawGraph();

        //Log Save Button
        btnLogSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                double weight = Double.parseDouble(etEnterWeight.getText().toString());
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                int index = 0;
                for(int i = 0; i< logEntryList.size(); i++){
                    if(logEntryList.get(i).getStartTime() == calendar.getTimeInMillis()){
                        index = i;
                        break;
                    }
                }

                LogEntry logEntry = new LogEntry(calendar.getTimeInMillis(),
                        logEntryList.get(index).getPlannedWeight(), weight);
                saveToDatabase(logEntry);
                logEntryList = new ArrayList<>();
                readItemsFromDatabase();
                drawGraph();
                drawGraph();
            }
        });


        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){

                    Intent intent;
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item){
                        switch (item.getItemId()){

                            case R.id.navigation_user:
                                intent = new Intent(LogActivity.this, UserActivity.class);
                                intent.putExtra("user", user);
                                LogActivity.this.startActivity(intent);
                                finish();
                                break;
                            case R.id.navigation_food:
                                intent = new Intent(LogActivity.this, FoodActivity.class);
                                intent.putExtra("user", user);
                                LogActivity.this.startActivity(intent);
                                finish();
                                break;
                            case R.id.navigation_log:
                                break;

                        }
                        return true;
                    }

                }
        );
    }

    private void drawGraph(){
        DataPoint[] plannedPoints = new DataPoint[logEntryList.size()];
        ArrayList<DataPoint> actualPointsList = new ArrayList<>();
        for(int i=0; i < logEntryList.size(); i++){
            plannedPoints[i] = new DataPoint(logEntryList.get(i).getStartTime(),
                    logEntryList.get(i).getPlannedWeight());
            if(logEntryList.get(i).getActualWeight() == 0.0){
                continue;
            }else{
                actualPointsList.add(new DataPoint(logEntryList.get(i).getStartTime(),
                        logEntryList.get(i).getActualWeight()));
            }
        }

        DataPoint[] actualPoints = new DataPoint[actualPointsList.size()];
        for (int i=0; i<actualPointsList.size(); i++ ){
            actualPoints[i] = actualPointsList.get(i);
        }

        graph = (GraphView) findViewById(R.id.graph);

        LineGraphSeries<DataPoint> plannedSeries = new LineGraphSeries<DataPoint>(plannedPoints);
        LineGraphSeries<DataPoint> actualSeries = new LineGraphSeries<DataPoint>(actualPoints);

        //set plannedSeries
        plannedSeries.setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
        plannedSeries.setDrawDataPoints(true);
        plannedSeries.setDataPointsRadius(10);
        plannedSeries.setTitle("Plan");

        //set actualSeries
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        paint.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        actualSeries.setDrawAsPath(true);
        actualSeries.setCustomPaint(paint);
        actualSeries.setDrawDataPoints(true);
        actualSeries.setDataPointsRadius(10);
        actualSeries.setTitle("Actual");



        // set view port
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.getViewport().setMinX(logEntryList.get(0).getStartTime());
        graph.getViewport().setMaxX(logEntryList.get(3).getStartTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(logEntryList.get(0).getStartTime());
        graph.getViewport().setMaxY(logEntryList.get(logEntryList.size()-1).getStartTime());
        graph.getViewport().setXAxisBoundsManual(true);
        // set grid label
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(2);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(null, nf));
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setVerticalAxisTitle("      ");
//        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");

        graph.getGridLabelRenderer().setNumHorizontalLabels(2);
        graph.getGridLabelRenderer().setNumVerticalLabels(5);
        graph.getGridLabelRenderer().setHumanRounding(false);

        //set legend
        LegendRenderer legendRenderer = graph.getLegendRenderer();
        legendRenderer.setVisible(true);
        legendRenderer.setAlign(LegendRenderer.LegendAlign.BOTTOM);
        legendRenderer.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorLight, null));

        //set listener
        plannedSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(LogActivity.this,
                        "Planned Weight: "+ String.format("%.2f",dataPoint.getY()) + " kg",
                        Toast.LENGTH_SHORT).show();
            }
        });

        actualSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(LogActivity.this,
                        "Actual Weight: "+ String.format("%.2f",dataPoint.getY()) + " kg",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // draw series
        graph.removeAllSeries();
        graph.addSeries(plannedSeries);
        graph.addSeries(actualSeries);
    }

    private void showTestCase(){
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();

        graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> test_series_1 = new LineGraphSeries<DataPoint>(new DataPoint[] {

                new DataPoint(d1, 60),
                new DataPoint(d2, 59.8),
                new DataPoint(d3, 59.6),
                new DataPoint(d4, 59.4),
                new DataPoint(d5, 59.2)
        });

        LineGraphSeries<DataPoint> test_series_2 = new LineGraphSeries<DataPoint>(new DataPoint[] {

                new DataPoint(d1, 60),
                new DataPoint(d2, 59.4),
                new DataPoint(d3, 59.8),
//                new DataPoint(d4, 59.2),
//                new DataPoint(d5, 59.6)
        });

        test_series_1.setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
      // test_series_2.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        test_series_1.setDrawDataPoints(true);
        test_series_1.setDataPointsRadius(10);
        test_series_2.setDrawDataPoints(true);
        test_series_2.setDataPointsRadius(10);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        paint.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        test_series_2.setDrawAsPath(true);
        test_series_2.setCustomPaint(paint);

        graph.addSeries(test_series_1);
        graph.addSeries(test_series_2);
    }


    private void readItemsFromDatabase() {
        try {
            new readItemsFromDatabase(this, dao).execute().get();
        }catch (Exception ex){
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }

    private static class readItemsFromDatabase extends AsyncTask<Void, Void, Void> {

        private final WeakReference<LogActivity> mActivityRef;
        private LogEntryDao dao;

        // Constructor
        readItemsFromDatabase(LogActivity _activity, LogEntryDao _dao){
            mActivityRef = new WeakReference<>(_activity);   // weak reference to prevent memory leak
            dao = _dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            LogActivity logActivity = mActivityRef.get();
            List<LogEntry> entriesFromDB = dao.listAll();

            logActivity.logEntryList.addAll(entriesFromDB);


            return null;
        }
    }


    private void saveToDatabase(LogEntry logEntry) {
        Log.i("xxx","saveToDatabase()"+logEntry);
        new saveToDatabase(this, dao).execute(logEntry);
    }

    private static class saveToDatabase extends AsyncTask<LogEntry, Void, Void> {

        private LogEntry logEntry;
        private LogEntryDao dao;

        saveToDatabase(LogActivity logActivity, LogEntryDao _dao){
            this.dao = _dao;
        }

        protected Void doInBackground(LogEntry... _logEntries) {
            dao.insert(_logEntries[0]);
            Log.i("xxx", "SQLite saved item: finish time = "+_logEntries[0]);
            return null;
        }
    }
}
