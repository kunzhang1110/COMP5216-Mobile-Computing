package au.edu.sydney.comp5216.easydiet.profile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Random;

import au.edu.sydney.comp5216.easydiet.R;
import au.edu.sydney.comp5216.easydiet.requests.UpdateRequest;
import au.edu.sydney.comp5216.easydiet.food.FoodActivity;
import au.edu.sydney.comp5216.easydiet.log.LogActivity;
import au.edu.sydney.comp5216.easydiet.log.LogEntry;
import au.edu.sydney.comp5216.easydiet.log.LogEntryDB;
import au.edu.sydney.comp5216.easydiet.log.LogEntryDao;

public class CalculatorActivity extends AppCompatActivity {

    EditText etTargetWeight;
    EditText etTargetDuration;
    Spinner spPal;
    Spinner spDiet;
    Button btnCalculate;
    Button btnSaveCalculation;
    TextView tvCalories;
    TextView tvCarboRangeLo;
    TextView tvCarboRangeHi;
    TextView tvProteinRangeLo;
    TextView tvProteinRangeHi;
    TextView tvFatRangeLo;
    TextView tvFatRangeHi;


    //database fields
    LogEntryDB db;
    LogEntryDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        etTargetWeight = (EditText) findViewById(R.id.etTargetWeight);
        etTargetDuration = (EditText) findViewById(R.id.etTargetDuration);
        spPal = (Spinner) findViewById(R.id.spPal);
        spDiet = (Spinner) findViewById(R.id.spDiet);
        btnCalculate =  (Button) findViewById(R.id.btnCalculate);
        btnSaveCalculation =  (Button) findViewById(R.id.btnSaveCalculation);
        tvCalories = (TextView) findViewById(R.id.tvCalories);
        tvCarboRangeLo = (TextView) findViewById(R.id.tvCarboRangeLo);
        tvCarboRangeHi = (TextView) findViewById(R.id.tvCarboRangeHi);
        tvProteinRangeLo = (TextView) findViewById(R.id.tvProteinRangeLo);
        tvProteinRangeHi = (TextView) findViewById(R.id.tvProteinRangeHi);
        tvFatRangeLo = (TextView) findViewById(R.id.tvFatRangeLo);
        tvFatRangeHi = (TextView) findViewById(R.id.tvFatRangeHi);
        final BottomNavigationView navigationView =  (BottomNavigationView) findViewById(R.id.navigationView);

        //Setup database
        db = LogEntryDB.getDatabase(this.getApplicationContext());
        dao = db.logEntryDao();

        Intent intent = getIntent();
        final User user = (User) intent.getSerializableExtra("user");

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(etTargetWeight.getText().toString())){
                    Toast.makeText(CalculatorActivity.this, "Please enter your target weight", Toast.LENGTH_SHORT).show();
                    return;
                }else if (Double.parseDouble(etTargetWeight.getText().toString()) > user.getWeight()){
                    Toast.makeText(CalculatorActivity.this, "Your target weight should be less than your starting weight.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(etTargetDuration.getText().toString())){
                    Toast.makeText(CalculatorActivity.this, "Please enter your target days", Toast.LENGTH_SHORT).show();
                    return;
                }

                double targetWeight = Double.parseDouble(etTargetWeight.getText().toString());
                int targetDuration = Integer.parseInt(etTargetDuration.getText().toString());
                int pal = spPal.getSelectedItemPosition();
                int dietChoice = spDiet.getSelectedItemPosition();

                user.setTargetWeight(targetWeight);
                user.setTargetDuration(targetDuration);
                user.setPal(pal);
                user.setDietChoice(dietChoice);


                DietCalculator dietCalculator = DietCalculatorFactory.getCalulator(user, dietChoice);
                double dailyCalorieTarget = dietCalculator.getDailyCalorieTarget();
                double dailyCarboTargetHi = dietCalculator.getDailyCarboTargetRange().get(1);
                double dailyCarboTargetLo = dietCalculator.getDailyCarboTargetRange().get(0);

                double dailyProteinTargetHi = dietCalculator.getDailyProteinTargetRange().get(1);
                double dailyProteinTargetLo = dietCalculator.getDailyProteinTargetRange().get(0);
                double dailyFatTargetHi = dietCalculator.getDailyFatTargetRange().get(1);
                double dailyFatTargetLo = dietCalculator.getDailyFatTargetRange().get(0);

                //Check Unrealistic Plan
                if(dietCalculator.getDailyCalorieTarget()< 200.0){
                    Toast.makeText(CalculatorActivity.this, "You need at least 200 Cal per day. Change your targets to make the plan more realistic", Toast.LENGTH_LONG).show();
                    return;
                }


                tvCalories.setText(String.format("%.2f", dailyCalorieTarget));
                tvCarboRangeLo.setText(String.format("%.2f", dailyCarboTargetLo));
                tvCarboRangeHi.setText(String.format("%.2f", dailyCarboTargetHi));
                tvProteinRangeLo.setText(String.format("%.2f", dailyProteinTargetLo));
                tvProteinRangeHi.setText(String.format("%.2f", dailyProteinTargetHi));
                if(dailyFatTargetLo == 0.00){
                    tvFatRangeLo.setText("--");
                    tvFatRangeHi.setText("--");
                }else {
                    tvFatRangeLo.setText(String.format("%.2f", dailyFatTargetLo));
                    tvFatRangeHi.setText(String.format("%.2f", dailyFatTargetHi));
                }


                user.setDailyCalorieTarget(dailyCalorieTarget);
                user.setDailyCarboTargetHi(dailyCarboTargetHi);
                user.setDailyCarboTargetLo(dailyCarboTargetLo);
                user.setDailyProteinTargetHi(dailyProteinTargetHi);
                user.setDailyProteinTargetLo(dailyProteinTargetLo);
                user.setDailyFatTargetHi(dailyFatTargetHi);
                user.setDailyFatTargetLo(dailyFatTargetLo);

            }
        }); // end of btnCalculate


        btnSaveCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveToServer(user);
                deleteAllFromDatabase();
                saveToRoom(user);
                Intent intent = new Intent();
                intent.putExtra("user",user);
                setResult(RESULT_OK, intent); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }
        });


        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){
                    Intent intent;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item){
                        switch (item.getItemId()){

                            case R.id.navigation_user:
                                intent = new Intent(CalculatorActivity.this, UserActivity.class);
                                intent.putExtra("user", user);
                                CalculatorActivity.this.startActivity(intent);
                                finish();
                                break;

                            case R.id.navigation_food:
                                intent = new Intent(CalculatorActivity.this, FoodActivity.class);
                                intent.putExtra("user", user);
                                CalculatorActivity.this.startActivity(intent);
                                finish();
                                break;
                            case R.id.navigation_log:
                                intent = new Intent(CalculatorActivity.this, LogActivity.class);
                                intent.putExtra("user", user);
                                CalculatorActivity.this.startActivity(intent);
                                finish();
                                break;
                        }
                        return true;
                    }

                }
        );
    }

    public void saveToServer(User user){
        Response.Listener<String> updateListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        Toast.makeText(CalculatorActivity.this, "saved" , Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorActivity.this);
                        builder.setMessage("Register Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        UpdateRequest registerRequest = new UpdateRequest(user,updateListener);
        RequestQueue queue = Volley.newRequestQueue(CalculatorActivity.this);
        queue.add(registerRequest);
    }

    public void saveToRoom(User user){
        double startingWeight = user.getWeight();
        double endingWeight = user.getTargetWeight();
        int duration = user.getTargetDuration();
        double weightLossPerDay = (startingWeight - endingWeight)/duration;//duration -1 ??

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        LogEntry logEntry = new LogEntry(calendar.getTimeInMillis(),startingWeight, user.getWeight());
        saveToDatabase(logEntry);

        double newWeight = startingWeight;

//        //test case
//        Random rand = new Random();
//        for(int i=0; i<duration; i++){//duration - 1 in total
//            calendar.add(Calendar.DATE, 1);
//            newWeight -= weightLossPerDay;
//            logEntry = new LogEntry(calendar.getTimeInMillis(),newWeight, newWeight + rand.nextDouble() -1);
//            saveToDatabase(logEntry);
//        }
        //real case
        for(int i=0; i<duration; i++){//duration - 1 in total
            calendar.add(Calendar.DATE, 1);
            newWeight -= weightLossPerDay;
            logEntry = new LogEntry(calendar.getTimeInMillis(),newWeight, 0.00);
            saveToDatabase(logEntry);
        }
    }

    //Database methods ------------------------------------------------
    private void saveToDatabase(LogEntry logEntry) {
        Log.i("xxx","saveToDatabase()"+logEntry);
        new saveToDatabase(this, dao).execute(logEntry);
    }

    //remove current table and saveToDatabase
    //TODO:add user specific
    private static class saveToDatabase extends AsyncTask<LogEntry, Void, Void> {

        private LogEntry logEntry;
        private LogEntryDao dao;

        saveToDatabase(CalculatorActivity calculatorActivity, LogEntryDao _dao){
            this.dao = _dao;
        }

        protected Void doInBackground(LogEntry... _logEntries) {
            dao.insert(_logEntries[0]);
            Log.i("xxx", "SQLite saved item: finish time = "+_logEntries[0]);
            return null;
        }
    }

    private void deleteAllFromDatabase() {
        new deleteAllFromDatabase(this, dao).execute();
    }

    private static class deleteAllFromDatabase extends AsyncTask<Void, Void, Void> {
        //Use asynchronous task to run query on the background and wait for result

        private final WeakReference<CalculatorActivity> mActivityRef;
        private LogEntryDao logEntryDao;
        Long id;

        // Constructor
        deleteAllFromDatabase(CalculatorActivity activity, LogEntryDao dao) {
            mActivityRef = new WeakReference<>(activity);
            logEntryDao = dao;
            this.id = id;
        }

        @Override
        // delete all
        protected Void doInBackground(Void... voids) { // ... == vargars == Variable Arguments
            logEntryDao.deleteAll();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }




}
