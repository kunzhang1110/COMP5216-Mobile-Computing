package au.edu.sydney.comp5216.easydiet.profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import au.edu.sydney.comp5216.easydiet.R;
import au.edu.sydney.comp5216.easydiet.food.FoodActivity;
import au.edu.sydney.comp5216.easydiet.log.LogActivity;

public class UserActivity extends AppCompatActivity {
    public final int CALCULATE_REQUEST_CODE = 1;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final TextView tvAge = (TextView) findViewById(R.id.tvAge);
        final TextView tvHeight = (TextView) findViewById(R.id.tvHeight);
        final TextView tvWeight = (TextView) findViewById(R.id.tvWeight);
        final TextView tvGender = (TextView) findViewById(R.id.tvGender);
        final TextView tvUserWeightTarget = (TextView) findViewById(R.id.tvUserWeightTarget);
        final TextView tvUserTargetDuration = (TextView) findViewById(R.id.tvUserTargetDuration);
        final TextView tvUserCalories = (TextView) findViewById(R.id.tvUserCalories);
        final TextView tvUserPlan = (TextView) findViewById(R.id.tvUserPlan);
        final Button btnToCalculation = (Button) findViewById(R.id.btnToCalculation);
        final BottomNavigationView navigationView =  (BottomNavigationView) findViewById(R.id.navigationView);

        //fill this line with a value for ACTIVITY

        final TextView welcome = (TextView) findViewById(R.id.welcomeTxt);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        welcome.setText(welcome.getText() + " " + user.getUserName() + "!");
        tvAge.setText(String.valueOf(user.getAge()));
        tvHeight.setText(String.valueOf(user.getHeight()));
        tvWeight.setText(String.format("%.1f", user.getWeight()));
        tvGender.setText(String.valueOf(user.getGender()));
        tvUserWeightTarget.setText(String.format("%.1f", user.getTargetWeight()));
        tvUserTargetDuration.setText(String.valueOf(user.getTargetDuration()));
        tvUserCalories.setText(String.format("%.0f", user.getDailyCalorieTarget()));

        if (user.getDietChoice() == 0){
            tvUserPlan.setText("Protein");
        }else{
            tvUserPlan.setText("Keto");
        }

        //set button
        btnToCalculation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserActivity.this, CalculatorActivity.class);
                intent.putExtra("user", user);
                UserActivity.this.startActivityForResult(intent, CALCULATE_REQUEST_CODE);
            }
        }); //end of BtnLogin.setOnClickListener


        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){
                    Intent intent;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item){
                        switch (item.getItemId()){

                            case R.id.navigation_user:
                                break;

                            case R.id.navigation_food:
                                intent = new Intent(UserActivity.this, FoodActivity.class);
                                intent.putExtra("user", user);
                                UserActivity.this.startActivity(intent);
                                finish();
                                break;
                            case R.id.navigation_log:
                                intent = new Intent(UserActivity.this, LogActivity.class);
                                intent.putExtra("user", user);
                                UserActivity.this.startActivity(intent);
                                finish();
                                break;
                        }
                        return true;
                    }

                }
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK){

            user = (User) intent.getSerializableExtra("user");
            final TextView tvUserWeightTarget = (TextView) findViewById(R.id.tvUserWeightTarget);
            final TextView tvUserTargetDuration = (TextView) findViewById(R.id.tvUserTargetDuration);
            final TextView tvUserCalories = (TextView) findViewById(R.id.tvUserCalories);
            final TextView tvUserPlan = (TextView) findViewById(R.id.tvUserPlan);

            tvUserWeightTarget.setText(String.valueOf(user.getTargetWeight()));
            tvUserTargetDuration.setText(String.valueOf(user.getTargetDuration()));
            tvUserCalories.setText(String.format("%.0f", user.getDailyCalorieTarget()));

            if (user.getDietChoice() == 0){
                tvUserPlan.setText("Protein");
            }else{
                tvUserPlan.setText("Keto");
            }

        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
