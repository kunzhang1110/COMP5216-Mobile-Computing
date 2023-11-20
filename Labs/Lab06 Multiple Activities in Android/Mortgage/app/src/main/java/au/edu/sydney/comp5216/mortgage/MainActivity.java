package au.edu.sydney.comp5216.mortgage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static Mortgage mortgage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mortgage = new Mortgage();
        setContentView(R.layout.activity_main);
    }

    public void modifyData(View v){
        Intent myIntent = new Intent(this, DataActivity.class);
        this.startActivity(myIntent);
    }

    public static final String MA = "MainActivity";

    protected void onStart(){
        super.onStart();
        Log.w(MA, "inside MainActivity:onStart \n");
        updateView();
    }

    protected void onRestart(){
        super.onRestart();
        Log.w(MA, "inside MainActivity:onRestart \n");
    }

    protected void onResume(){
        super.onResume();
        Log.w(MA, "inside MainActivity:onResume \n");
    }

    protected void onPause(){
        super.onPause();
        Log.w(MA, "inside MainActivity:onPause \n");
    }

    protected void onStop(){
        super.onStop();
        Log.w(MA, "inside MainActivity:onStop \n");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.w(MA, "inside MainActivity:onDestroy \n");
    }

    public void updateView(){
        TextView amountTV = (TextView) findViewById(R.id.amount);
        amountTV.setText(mortgage.getFormattedAmount());

        TextView yearsTV = (TextView) findViewById(R.id.years);
        yearsTV.setText(""+mortgage.getYears());

        TextView rateTV = (TextView) findViewById(R.id.rate);
        rateTV.setText(100*mortgage.getRate()+"%");

        TextView monthlyTV = (TextView) findViewById(R.id.payment);
        monthlyTV.setText(mortgage.formattedMonthlyPayment());

        TextView totalTV = (TextView) findViewById(R.id.total);
        totalTV.setText(mortgage.formattedTotalPayment());
    }
}
