package au.edu.sydney.comp5216.easydiet.login;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import au.edu.sydney.comp5216.easydiet.R;
import au.edu.sydney.comp5216.easydiet.requests.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etAge = (EditText) findViewById(R.id.edAge);
        final EditText etName = (EditText) findViewById(R.id.edName);
        final EditText etUsername = (EditText) findViewById(R.id.edUserName);
        final EditText etPassword = (EditText) findViewById(R.id.edPassword);
        final EditText etHeight = (EditText) findViewById(R.id.edHeight);
        final EditText etWeight = (EditText) findViewById(R.id.edWeight);
        final Spinner spGender = (Spinner) findViewById(R.id.spGender);
        final Button bRegister = findViewById(R.id.btRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(TextUtils.isEmpty(etName.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Please enter your name ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(etUsername.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Please enter your username ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(etAge.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Please enter your age ", Toast.LENGTH_SHORT).show();
                    return;
                }else if (Integer.parseInt(etAge.getText().toString()) > 230||
                        Float.parseFloat(etAge.getText().toString()) < 1
                        ){
                    Toast.makeText(RegisterActivity.this, "Your age is not correct", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(etPassword.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Please enter your password ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(etHeight.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Please enter your height ", Toast.LENGTH_SHORT).show();
                    return;
                }else if (Float.parseFloat(etHeight.getText().toString()) > 250.0 ||
                        Float.parseFloat(etHeight.getText().toString()) < 50.0
                        ){
                    Toast.makeText(RegisterActivity.this, "Your height is not correct", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(etWeight.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Please enter your weight", Toast.LENGTH_SHORT).show();
                    return;
                }else if (Float.parseFloat(etWeight.getText().toString()) > 500.0 ||
                    Float.parseFloat(etWeight.getText().toString()) < 10.0){
                    Toast.makeText(RegisterActivity.this, "Your weight is not correct", Toast.LENGTH_SHORT).show();
                    return;
                }


                final String name = etName.getText().toString();
                final int age = Integer.parseInt(etAge.getText().toString());
                final String userName = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String gender = spGender.getSelectedItem().toString();
                final float height = Float.parseFloat(etHeight.getText().toString());
                final float weight = Float.parseFloat(etWeight.getText().toString());



                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);

                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

                RegisterRequest registerRequest = new RegisterRequest(name, userName, age, password,
                        height, weight, gender, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });

    }
}
