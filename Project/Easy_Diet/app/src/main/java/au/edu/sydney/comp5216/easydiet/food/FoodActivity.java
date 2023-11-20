package au.edu.sydney.comp5216.easydiet.food;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.StringBufferInputStream;
import java.net.URL;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Random;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import au.edu.sydney.comp5216.easydiet.R;
import au.edu.sydney.comp5216.easydiet.log.LogActivity;
import au.edu.sydney.comp5216.easydiet.profile.User;
import au.edu.sydney.comp5216.easydiet.profile.UserActivity;


public class FoodActivity extends AppCompatActivity {
    EditText userInput;
    TextView calorieLable;
    TextView calorieAmount;
    TextView tvEatenCalories;
    //TextView calorieRemainLabel;
    TextView calorieRemain;
    TextView mTextView;
    double calorieSum = 0;
    double dailyIntake;
    double remaining = 0;
    double remaining2;
    ArrayList<String> foodRecommand = new ArrayList<String>();
    JSONObject jsonResponse;

    private static final String LOGIN_REQUEST_URL = "https://covert-hips.000webhostapp.com/Foods.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        userInput = findViewById(R.id.inputDaily);
        //calorieLable = findViewById(R.id.textView);
        calorieAmount = findViewById(R.id.calorieAmount);
        tvEatenCalories = findViewById(R.id.tvEatenCalories);
        //mTextView = (TextView) findViewById(R.id.textt);
        //calorieRemain = (TextView) findViewById(R.id.calorieRemain);
        Button calculateButton = (Button) this.findViewById(R.id.calculateButton);
        final BottomNavigationView navigationView =  (BottomNavigationView) findViewById(R.id.navigationView);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calorieSum = sendPost();
            }
        });
        Button testing = (Button)this.findViewById(R.id.testing);

        Intent intent = getIntent();
        final User user = (User) intent.getSerializableExtra("user");
        dailyIntake = user.getDailyCalorieTarget();

        testing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //calorieRemain.setText(Double.toString(dailyIntake-calorieSum));
                remaining = dailyIntake-calorieSum;
                remaining2 = remaining;
                Log.i("remaining", String.valueOf(remaining));
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            jsonResponse = new JSONObject(response);
                            //boolean success = jsonResponse.getBoolean("success");

                            //Log.i("qR",queryResult);
                            //String food_id = jsonResponse.getJSONObject("4").getString("Name");
                            //Integer food_id2 = jsonResponse.getInt("food_id");

                            //Log.i("foodID", String.valueOf(food_id));
                            //Log.i("foodID2", String.valueOf(food_id2));
                            generateFoodRecommand(jsonResponse);
                            Intent intent = new Intent(FoodActivity.this, FoodListActivity.class);
                            for (int i = 0; i < foodRecommand.size();i++){
                                intent.putExtra(String.valueOf(i),"Name: "+jsonResponse.getJSONObject(foodRecommand.get(i)).getString("item_name") + "\n\t" +"- Protein: "+jsonResponse.getJSONObject(foodRecommand.get(i)).getString("Protein") + "\n\t" + "- Calorie: "+jsonResponse.getJSONObject(foodRecommand.get(i)).getString("Cal") + "\n\t"+ "- Fat: " + jsonResponse.getJSONObject(foodRecommand.get(i)).getString("Fat") );
                            }
                            intent.putExtra("Size",String.valueOf(foodRecommand.size()));

                            generateFoodRecommand(jsonResponse);
                            for (int i = 0; i < foodRecommand.size();i++){
                                intent.putExtra(String.valueOf(i)+100,"Name: "+jsonResponse.getJSONObject(foodRecommand.get(i)).getString("item_name") + "\n\t" +"- Protein: "+jsonResponse.getJSONObject(foodRecommand.get(i)).getString("Protein") + "\n\t" + "- Calorie: "+jsonResponse.getJSONObject(foodRecommand.get(i)).getString("Cal") + "\n\t"+ "- Fat: " + jsonResponse.getJSONObject(foodRecommand.get(i)).getString("Fat") );
                            }
                            intent.putExtra("Size2",String.valueOf(foodRecommand.size()));

                            generateFoodRecommand(jsonResponse);
                            for (int i = 0; i < foodRecommand.size();i++){
                                intent.putExtra(String.valueOf(i)+200,"Name: "+jsonResponse.getJSONObject(foodRecommand.get(i)).getString("item_name") + "\n\t" +"- Protein: "+jsonResponse.getJSONObject(foodRecommand.get(i)).getString("Protein") + "\n\t" + "- Calorie: "+jsonResponse.getJSONObject(foodRecommand.get(i)).getString("Cal") + "\n\t"+ "- Fat: " + jsonResponse.getJSONObject(foodRecommand.get(i)).getString("Fat") );
                            }
                            intent.putExtra("Size3",String.valueOf(foodRecommand.size()));
                            intent.putExtra("user", user);
                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                };
                StringRequest loginRequest = new StringRequest(LOGIN_REQUEST_URL,responseListener,null);
                RequestQueue queue = Volley.newRequestQueue(FoodActivity.this);
                queue.add(loginRequest);
            }
        });

        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){

                    Intent intent;
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item){
                        switch (item.getItemId()){

                            case R.id.navigation_user:
                                intent = new Intent(FoodActivity.this, UserActivity.class);
                                intent.putExtra("user", user);
                                FoodActivity.this.startActivity(intent);
                                finish();
                                break;
                            case R.id.navigation_food:
                                break;
                            case R.id.navigation_log:
                                intent = new Intent(FoodActivity.this, LogActivity.class);
                                intent.putExtra("user", user);
                                FoodActivity.this.startActivity(intent);
                                finish();
                                break;

                        }
                        return true;
                    }

                }
        );
    }

    public double sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://trackapi.nutritionix.com/v2/natural/nutrients");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("x-app-id", "9cf573d1");
                    conn.setRequestProperty("x-app-key", "d7c24f59383c1d5015f989dd55a4db8b");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("query", userInput.getText().toString());
                    jsonParam.put("timezone", "US/Eastern");

                    //jsonParam.put("x-remote-user-id","0");

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());
                    Log.i("content", String.valueOf(conn.getInputStream()));
                    String a = convertStreamToString(conn.getInputStream());
                    Log.i("data", a);

                    int indexC = a.indexOf("calories");
                    int indexN = a.indexOf("nf_total_fat");
                    calorieSum = 0;
                    while (indexC >= 0) {
                        calorieSum += Double.parseDouble(a.substring(indexC + 10, indexN - 2));
                        indexC = a.indexOf("calories", indexC + 1);
                        indexN = a.indexOf("nf_total_fat", indexN + 1);
                    }

                    Log.i("SUM", String.valueOf(calorieSum));
                    a = a.substring(a.indexOf("calories") + 10, a.indexOf("nf_total_fat") - 2);
                    Log.i("data", a);

                    remaining = dailyIntake - calorieSum;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvEatenCalories.setText(String.format("%.2f", calorieSum));
                            calorieAmount.setText(String.format("%.2f", remaining));
                        }
                    });


                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
        return calorieSum;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    public ArrayList<String> generateFoodRecommand( JSONObject jsonResponse) throws JSONException {
        foodRecommand.clear();
        int a = new Random().nextInt(260);
        a+=1;
        while (Double.parseDouble(jsonResponse.getJSONObject(String.valueOf(a)).getString("Cal"))<(dailyIntake-calorieSum)*0.6 && Double.parseDouble(jsonResponse.getJSONObject(String.valueOf(a)).getString("Cal"))<dailyIntake-calorieSum){
            a = new Random().nextInt(260);
            a+=1;
        }

        foodRecommand.add(String.valueOf(a));
        Log.i("ADDED", String.valueOf(a));
        remaining -= Double.parseDouble(jsonResponse.getJSONObject(String.valueOf(a)).getString("Cal"));
        a = new Random().nextInt(260);
        a+=1;
        int i = 0;
        while (remaining>0&&i<20){

            if (Double.parseDouble(jsonResponse.getJSONObject(String.valueOf(a)).getString("Cal"))<remaining && !foodRecommand.contains(String.valueOf(a))&& Double.parseDouble(jsonResponse.getJSONObject(String.valueOf(a)).getString("Protein"))!=0 && Double.parseDouble(jsonResponse.getJSONObject(String.valueOf(a)).getString("Fat"))!=0){
                foodRecommand.add(String.valueOf(a));
                Log.i("ADDED", String.valueOf(a));
                remaining -= Double.parseDouble(jsonResponse.getJSONObject(String.valueOf(a)).getString("Cal"));
            }
            else{
                a = new Random().nextInt(260);
                a+=1;
                i++;
            }
        }
        Log.i("a", foodRecommand.toString());
        remaining = remaining2;
        return foodRecommand;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("shake","shake");



    }



}

