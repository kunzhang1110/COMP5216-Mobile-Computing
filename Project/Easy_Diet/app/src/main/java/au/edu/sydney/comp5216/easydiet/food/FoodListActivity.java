package au.edu.sydney.comp5216.easydiet.food;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import au.edu.sydney.comp5216.easydiet.R;
import au.edu.sydney.comp5216.easydiet.log.LogActivity;
import au.edu.sydney.comp5216.easydiet.profile.CalculatorActivity;
import au.edu.sydney.comp5216.easydiet.profile.User;
import au.edu.sydney.comp5216.easydiet.profile.UserActivity;

public class FoodListActivity extends AppCompatActivity{
    // Define variables
    ListView listView;
    //ListView dateView;
    ArrayList<String> items;
    ArrayList<String> items2;
    ArrayList<String> items3;
    //ArrayList<String> dates;
    ArrayAdapter<String> itemsAdapter;
    ArrayAdapter<String> datesAdapter;
    EditText addItemEditText;
    TextView textView;
    public final String shared_prefs = "sharedPrefs";
    //ToDoItemDao toDoItemDao;
    TextView ASlabel;
    TextView averageSpeed;
    //ToDoItemDB db;
    public double sum;
    int shaketime =0;

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use "activity_main.xml" as the layout
        setContentView(R.layout.activity_foodlist);
        final BottomNavigationView navigationView =  (BottomNavigationView) findViewById(R.id.navigationView);
        // Reference the "listView" variable to the id "lstView" in the layout
        items = new ArrayList<String>();
        items2 = new ArrayList<String>();
        items3 = new ArrayList<String>();
        shaketime=0;

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MainAdapter(items);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, String data)
            {
                //Toast.makeText(FoodListActivity.this,"data:"+data,Toast.LENGTH_SHORT).show();
            }
        });

        String size = getIntent().getExtras().getString("Size");
        String size2 = getIntent().getExtras().getString("Size2");
        String size3 = getIntent().getExtras().getString("Size3");
        final User user = (User) getIntent().getSerializableExtra("user");
        Log.i("size",size);
        Log.i("size2",size2);
        Log.i("size3",size3);

        for (int i =0; i < Integer.parseInt(size);i++){
            String foodItem = getIntent().getExtras().getString(String.valueOf(i));
            items.add(foodItem);
        }
        for (int i =0; i < Integer.parseInt(size2);i++){
            String foodItem2 = getIntent().getExtras().getString(String.valueOf(i)+100);
            items2.add(foodItem2);
        }
        for (int i =0; i < Integer.parseInt(size3);i++){
            String foodItem2 = getIntent().getExtras().getString(String.valueOf(i)+200);
            items3.add(foodItem2);
        }
        Log.i("items2", String.valueOf(items2));
        Log.i("items3", String.valueOf(items3));

        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){
                    Intent intent;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item){
                        switch (item.getItemId()){

                            case R.id.navigation_user:
                                intent = new Intent(FoodListActivity.this, UserActivity.class);
                                intent.putExtra("user", user);
                                FoodListActivity.this.startActivity(intent);
                                finish();
                                break;

                            case R.id.navigation_food:
                                intent = new Intent(FoodListActivity.this, FoodActivity.class);
                                intent.putExtra("user", user);
                                FoodListActivity.this.startActivity(intent);
                                finish();
                                break;
                            case R.id.navigation_log:
                                intent = new Intent(FoodListActivity.this, LogActivity.class);
                                intent.putExtra("user", user);
                                FoodListActivity.this.startActivity(intent);
                                finish();
                                break;
                        }
                        return true;
                    }

                }
        );

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (requestCode == EDIT_ITEM_REQUEST_CODE) {
        if(1==1){
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                String editedItem = data.getExtras().getString("msg");
                //String editedDate = data.getExtras().getString("date");

                int position = data.getIntExtra("position", -1);
                Log.i("Updated Item in list:", editedItem + ",position:"
                        + position);
                if (position == -1) {
                    items.add(0, editedItem);
                    //dates.add(0, editedDate);
                    itemsAdapter.notifyDataSetChanged();
                    //datesAdapter.notifyDataSetChanged();
                } else if (position == -2) {
                } else {
                    items.set(position, editedItem);
                    //dates.set(position, editedDate);
                    int index1 = items.indexOf(editedItem);
                    items.remove(index1);
                    items.add(0, editedItem);
                    //int index2 = dates.indexOf(editedDate);
                    //dates.remove(index2);
                    //dates.add(0, editedDate);
                    Log.i("Updated Item in list:", editedItem + ",position:"
                            + position);
                    Toast.makeText(this, "updated:" + editedItem, Toast.LENGTH_SHORT).show();
                    itemsAdapter.notifyDataSetChanged();
                    //datesAdapter.notifyDataSetChanged();
                }
                //Log.i("date", dates.toString());
                Log.i("items", items.toString());
                //save();
                // saveItemsToDatabase();

            }

        }
    }

    public void save() {
        SharedPreferences sharedPreferences = getSharedPreferences(shared_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < items.size(); i++) {
            editor.putString("item" + i, items.get(i));
        }


        editor.apply();
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();


    }
    public void shake(View v) {
        if (shaketime == 0){
            mAdapter = new MainAdapter(items2);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mAdapter);
            shaketime++;
        }
        else if (shaketime == 1){
            mAdapter = new MainAdapter(items3);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mAdapter);
        }


    }

    public void load() {
        SharedPreferences sharedPreferences = getSharedPreferences(shared_prefs, MODE_PRIVATE);
        int itemsize = sharedPreferences.getInt("itemsize", 0);
        for (int i = 0; i < itemsize; i++) {
            String loadedItem = sharedPreferences.getString("item" + i, "not found");
            items.add(loadedItem);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

