package au.edu.sydney.comp5216.querydatafromcloud;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //detect if code have some slow operation/memory leak
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // initialize variable
        items = new ArrayList<String>();

        // widget references
        listview = (ListView)findViewById(R.id.listview);
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        // set adapters
        listview.setAdapter(itemsAdapter);
    }

    public void onAddItemClick(View view) throws Exception{
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
