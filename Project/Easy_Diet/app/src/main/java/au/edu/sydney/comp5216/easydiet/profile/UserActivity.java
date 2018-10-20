package au.edu.sydney.comp5216.easydiet.profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import au.edu.sydney.comp5216.easydiet.R;
import au.edu.sydney.comp5216.easydiet.food.FoodActivity;
import au.edu.sydney.comp5216.easydiet.log.LogActivity;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final TextView tvAge = (TextView) findViewById(R.id.tvAge);
        final TextView tvHeight = (TextView) findViewById(R.id.tvHeight);
        final TextView tvWeight = (TextView) findViewById(R.id.tvWeight);
        final TextView tvGender = (TextView) findViewById(R.id.tvGender);
        final Button btnToCalculation = (Button) findViewById(R.id.btnToCalculation);
        final BottomNavigationView navigationView =  (BottomNavigationView) findViewById(R.id.navigationView);

        //fill this line with a value for ACTIVITY

        final TextView welcome = (TextView) findViewById(R.id.welcomeTxt);

        Intent intent = getIntent();
        final User user = (User) intent.getSerializableExtra("user");

        welcome.setText(welcome.getText() + " " + user.getUserName() + "!");
        tvAge.setText(String.valueOf(user.getAge()));
        tvHeight.setText(String.valueOf(user.getHeight()));
        tvWeight.setText(String.valueOf(user.getWeight()));
        tvGender.setText(String.valueOf(user.getGender()));


        //set button
        btnToCalculation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserActivity.this, CalculatorActivity.class);
                intent.putExtra("user", user);
                UserActivity.this.startActivity(intent);
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





}
