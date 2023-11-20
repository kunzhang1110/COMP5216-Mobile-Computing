package au.edu.sydney.comp5216.easydiet.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import au.edu.sydney.comp5216.easydiet.R;
import au.edu.sydney.comp5216.easydiet.requests.LoginRequest;
import au.edu.sydney.comp5216.easydiet.profile.User;
import au.edu.sydney.comp5216.easydiet.profile.UserActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.edUser);
        final EditText etPassword = (EditText) findViewById(R.id.EdPasswordLogin);

        final Button BtnLogin = findViewById(R.id.btLogin);

        final TextView registerLink = (TextView) findViewById(R.id.txtRegister);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(register);
            }
        });

        BtnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(TextUtils.isEmpty(etUsername.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Please enter your username ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(etPassword.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Please enter your password ", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String userName = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
//                            Log.i("xxxResponse", "["+response+"]");
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            Log.i("xxxResponse", "["+response+"]");
                            if (success) {

                                User user = new User(
                                        jsonResponse.getInt("userId"),
                                        jsonResponse.getString("name"),
                                        jsonResponse.getString("userName"),
                                        null,
                                        jsonResponse.getInt("age"),
                                        jsonResponse.getInt("height"),
                                        jsonResponse.getDouble("weight"),
                                        jsonResponse.getInt("pal"),
                                        jsonResponse.getString("gender"),

                                        jsonResponse.getInt("dietChoice"),
                                        jsonResponse.getDouble("targetWeight"),
                                        jsonResponse.getInt("targetDuration"),

                                        jsonResponse.getDouble("dailyCarboTargetHi"),
                                        jsonResponse.getDouble("dailyCarboTargetLo"),
                                        jsonResponse.getDouble("dailyProteinTargetHi"),
                                        jsonResponse.getDouble("dailyProteinTargetLo"),
                                        jsonResponse.getDouble("dailyFatTargetHi"),
                                        jsonResponse.getDouble("dailyFatTargetLo"),
                                        jsonResponse.getDouble("dailyCalorieTarget")
                                );

                                Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                                intent.putExtra("user", user);
                                LoginActivity.this.startActivity(intent);

                            } else {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                                builder.setMessage("\nLogin Failed. \n\nCheck your username and password.\n")
//                                        .create()
//                                        .show();
                                Toast.makeText(LoginActivity.this, "Login Failed. Check your username and password.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }; // end of response listener

                LoginRequest loginRequest = new LoginRequest(userName, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        }); //end of BtnLogin.setOnClickListener
    }
}
