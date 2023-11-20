package au.edu.sydney.comp5216.mortgage;

import android.opengl.EGLDisplay;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;

public class DataActivity extends AppCompatActivity {

    public void onCreate( Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        updateView();
    }

    public void updateView() {
        // Get a reference to our Mortgage object
        Mortgage mortgage = MainActivity.mortgage;

        // Update the view
        // First update the radio buttons
        if (mortgage.getYears() == 10  ) {
            RadioButton rb10 = (RadioButton) findViewById(R.id.ten);
            rb10.setChecked(true);
        } else if (mortgage.getYears() == 15) {
            RadioButton rb15 = (RadioButton) findViewById(R.id.fifteen);
            rb15.setChecked(true);
        } // else do nothing (default is 30)

        EditText amountET = (EditText) findViewById(R.id.data_amount);
        amountET.setText(""+mortgage.getAmount());
        EditText rateET = (EditText) findViewById(R.id.data_rate);
        rateET.setText(""+mortgage.getRate());
    }

    public void updateMortgageObject(){
        Mortgage mortgage = MainActivity.mortgage;
        RadioButton rb10 = (RadioButton) findViewById(R.id.ten);
        RadioButton rb15 = (RadioButton) findViewById(R.id.fifteen);
        RadioButton rb30 = (RadioButton) findViewById(R.id.thirty);

        int years = 30;
        if (rb10.isChecked()){
            years = 10;
        } else if (rb15.isChecked()){
            years = 15;
        }
        mortgage.setYears(years);

        EditText amountET = (EditText) findViewById(R.id.data_amount);
        String amountString = amountET.getText().toString();
        EditText rateET = (EditText) findViewById(R.id.data_rate);
        String rateString = rateET.getText().toString();

        try{
            float amount = Float.parseFloat(amountString);
            mortgage.setAmount(amount);

            float rate = Float.parseFloat(rateString);
            mortgage.setRate(rate);
        } catch (NumberFormatException nfe) {
            mortgage.setAmount(100000.0f);
            mortgage.setRate(.034f);
        }
    }

    public void goBack(View v){
        updateMortgageObject();
        this.finish();
    }

    public static final String DA = "MainActivity";

    protected void onStart(){
        super.onStart();
        Log.w(DA, "inside DataActivity:onStart \n");
    }

    protected void onRestart(){
        super.onRestart();
        Log.w(DA, "inside DataActivity:onRestart \n");
    }

    protected void onResume(){
        super.onResume();
        Log.w(DA, "inside DataActivity:onResume \n");
    }

    protected void onPause(){
        super.onPause();
        Log.w(DA, "inside DataActivity:onPause \n");
    }

    protected void onStop(){
        super.onStop();
        Log.w(DA, "inside DataActivity:onStop \n");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.w(DA, "inside DataActivity:onDestroy \n");
    }

}
