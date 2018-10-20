package au.edu.sydney.comp5216.easydiet.food;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import au.edu.sydney.comp5216.easydiet.R;

public class FoodListActivity extends AppCompatActivity {
    // Define variables
    ListView listView;
    //ListView dateView;
    ArrayList<String> items;
    //ArrayList<String> dates;
    ArrayAdapter<String> itemsAdapter;
    ArrayAdapter<String> datesAdapter;
    EditText addItemEditText;
    public final String shared_prefs = "sharedPrefs";
    //ToDoItemDao toDoItemDao;
    TextView ASlabel;
    TextView averageSpeed;
    //ToDoItemDB db;
    public double sum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use "activity_food.xml" as the layout
        setContentView(R.layout.activity_foodlist);
        // Reference the "listView" variable to the id "lstView" in the layout
        listView = (ListView) findViewById(R.id.lstView);
        //dateView = (ListView) findViewById(R.id.dateView);
        //db = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        //toDoItemDao = db.toDoItemDao();
        // Create an ArrayList of String
        //SharedPreferences sp = getSharedPreferences("itemsndates", Context.MODE_PRIVATE);
        //ASlabel = (TextView)findViewById(R.id.ASlabel);
        //averageSpeed = (TextView)findViewById(R.id.averageSpeed);
        items = new ArrayList<String>();

        //dates = new ArrayList<String>();

        // Create an adapter for the list view using Android's built-in item layout
        //readItemsFromDatabase();
        //load();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //datesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dates);
        // Connect the listView and the adapter
        //readItemsFromDatabase();
        listView.setAdapter(itemsAdapter);
        //dateView.setAdapter(datesAdapter);
        setupListViewListener();


        String size = getIntent().getExtras().getString("Size");
        Log.i("size","size");
        Log.i("size",size);
        for (int i =0; i < Integer.parseInt(size);i++){
            String foodItem = getIntent().getExtras().getString(String.valueOf(i));
            items.add(foodItem);
        }
//        items.set(0, editedItem);
//        //dates.set(position, editedDate);
//        int index1 = items.indexOf(editedItem);
//        items.remove(index1);
        //items.add(editedItem);
        //int index2 = dates.indexOf(editedDate);
        //dates.remove(index2);
        //dates.add(0, editedDate);
//        Log.i("Updated Item in list:", editedItem + ",position:"
//                + 0);
//        Toast.makeText(this, "updated:" + editedItem, Toast.LENGTH_SHORT).show();
        itemsAdapter.notifyDataSetChanged();
        //saveItemsToDatabase();
//        for (int i = 0; i < items.size();i++){
//            sum+=Double.parseDouble(items.get(i).substring(0,3));
//        }
//        sum = sum/items.size();
//        averageSpeed.setText(Double.toString(sum));
        Button back = (Button)this.findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

    }


//    public void onAddItemClicki(View view) {
//        //String toAddString = addItemEditText.getText().toString();
//        Intent intent = new Intent(FoodActivity.this, EditToDoItemActivity.class);
//        startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
//        //itemsAdapter.notifyDataSetChanged();
////        if (toAddString != null && toAddString.length() > 0) {
////            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
////            itemsAdapter.add(toAddString); // Add text to list view adapter
////            //dateAdapter.add(date);
////            datesAdapter.add(date);
////            addItemEditText.setText(""); }
//    }

    private void setupListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long rowId) {
                Log.i("FoodActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodListActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        items.remove(position); // Remove item from the ArrayList
                                        //dates.remove(position);
                                        itemsAdapter.notifyDataSetChanged(); // Notify listView adapter to update the list
                                        //datesAdapter.notifyDataSetChanged();
                                        //save();
                                       // saveItemsToDatabase();

                                    }
                                })
                        .setNegativeButton(R.string.cancel, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User cancelled the dialog
                                        // Nothing happens
                                    }
                                });
                builder.create().show();
                return true;
            }
        });


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String updateItem = (String) itemsAdapter.getItem(position);
//                Log.i("FoodActivity", "Clicked item " + position + ": " + updateItem);
//                Intent intent = new Intent(FoodActivity.this, EditToDoItemActivity.class);
//                if (intent != null) {
//                    // put "extras" into the bundle for access in the edit activity
//                    intent.putExtra("item", updateItem);
//                    intent.putExtra("position", position);
//                    // brings up the second activity
//                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
//                    itemsAdapter.notifyDataSetChanged();
//                }
//            }
//        });
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
//        editor.putInt("itemsize", items.size());
//        for (int i = 0; i < dates.size(); i++) {
//            editor.putString("date" + i, dates.get(i));
//        }


        //editor.putInt("datesize",dates.size());

        editor.apply();
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();


    }

    public void load() {
        SharedPreferences sharedPreferences = getSharedPreferences(shared_prefs, MODE_PRIVATE);
        int itemsize = sharedPreferences.getInt("itemsize", 0);
        for (int i = 0; i < itemsize; i++) {
            String loadedItem = sharedPreferences.getString("item" + i, "not found");
            items.add(loadedItem);
        }
//        for (int i = 0; i < itemsize; i++) {
//            String loadedItem = sharedPreferences.getString("date" + i, "not found");
//            dates.add(loadedItem);
//        }
    }







//    private void readItemsFromDatabase()
//    {
//        //Use asynchronous task to run query on the background and wait for result
//        try {
//            new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... voids) {
//                    //read items from database
//                    List<ToDoItem> itemsFromDB = toDoItemDao.listAll();
//                    items = new ArrayList<String>();
//                    if (itemsFromDB != null & itemsFromDB.size() > 0) {
//                        for (ToDoItem item : itemsFromDB) {
//                            items.add(item.getToDoItemName());
//                            Log.i("SQLite read item", "ID: " + item.getToDoItemID() + " Name: " + item.getToDoItemName());
//                        }
//                    }
//                    return null;
//                }
//            }.execute().get();
//        }
//        catch(Exception ex) {
//            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
//        }
//    }
//
//    private void saveItemsToDatabase()
//    {
//        //Use asynchronous task to run query on the background to avoid locking UI
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                //delete all items and re-insert
//                toDoItemDao.deleteAll();
//                for (String todo : items) {
//                    ToDoItem item = new ToDoItem(todo);
//                    toDoItemDao.insert(item);
//                    Log.i("SQLite saved item", todo);
//                }
//                return null;
//            }
//        }.execute();
//    }

}

