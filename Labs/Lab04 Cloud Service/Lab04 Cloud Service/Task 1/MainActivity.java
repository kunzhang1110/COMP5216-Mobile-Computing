package comp5216.sydney.edu.au.comp5216w04;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    //Define variables
    ListView listview;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    EditText addItemEditText;

    private final String USER_AGENT = "Mozilla/5.0";
    public List<Double> temperatures;
    public String httpresponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Use "activity_main.xml" as the layout
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Reference the "listview" variable to the id-"listview" in the layout
        listview = (ListView)findViewById(R.id.listview);

        //Create an ArrayList of String
        items = new ArrayList<String>();

        //Create an adapter for the list view using Android's built-in item layout
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);

        //Connect the listview and the adapter
        listview.setAdapter(itemsAdapter);
    }


    public void onAddItemClick(View view) throws Exception {

        itemsAdapter.clear();

        ParseFromCloud temperatures = new ParseFromCloud();
        temperatures.getTemperature();
        ArrayList<TemperatureData> tds = temperatures.tds;

        for (TemperatureData td: tds) {
            String toAddString = td.getCreatedAt() + "\nTemperature is: " + td.getTemperature();
            if (toAddString != null && toAddString.length() > 0) {
                itemsAdapter.add(toAddString);
            }
        }
    }
}
