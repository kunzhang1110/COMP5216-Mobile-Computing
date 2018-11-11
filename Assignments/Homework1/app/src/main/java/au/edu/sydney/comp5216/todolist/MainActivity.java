package au.edu.sydney.comp5216.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.lang.ref.WeakReference;
import java.util.Map;

import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    LinkedHashMap<Long, ArrayList<String>> dataRows;
    SimpleAdapter itemsAdapter;
    public final int ADD_ITEM_REQUEST_CODE = 1;
    public final int EDIT_ITEM_REQUEST_CODE = 2;
    List<LinkedHashMap<String, String>> listItems;

    ToDoItemDB db;
    ToDoItemDao toDoItemDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize variables
        listView = (ListView) findViewById(R.id.listView);

        db = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        toDoItemDao = db.toDoItemDao();
        dataRows = new LinkedHashMap<>(); // (itemTime, (itemID, itemName)) LinkedHashMap is used for sorting

        //setups
        setupListViewAdapter();
        setupListViewListener();
    }

//    @Override
//    //update listView on resume
//    protected void onResume() {
//
//        super.onResume();
//        setupListViewAdapter();
//    }

    // Button addNew Click Action
    public void onAddNewClick(View view) {
        Intent intent = new Intent(MainActivity.this, EditAddItemActivity.class);
        startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);

    }

    // Setup double-line List using SimpleAdapter
    private void setupListViewAdapter() {
        readItemsFromDatabase();    //store in dataRows
        listItems = new ArrayList<>();
        Log.i("dataRows:", "dataRows:" + dataRows.keySet() + dataRows.values());

        //store data in ArrayList listItems to be used for SimpleAdapter
        Iterator<Map.Entry<Long, ArrayList<String>>> itr = dataRows.entrySet().iterator();
        while (itr.hasNext()) {
            LinkedHashMap<String, String> resultsMap = new LinkedHashMap<>();
            Map.Entry<Long, ArrayList<String>> dataRow = itr.next();
            ArrayList<String> dataRowValue = dataRow.getValue();
            resultsMap.put("Item Name", dataRowValue.get(0));
            resultsMap.put("Item Time", dataRowValue.get(1));
            listItems.add(resultsMap);
        }

        itemsAdapter = new SimpleAdapter(this, listItems, android.R.layout.simple_list_item_2,
                new String[]{"Item Name", "Item Time"},
                new int[]{android.R.id.text1, android.R.id.text2});

        // Connect the listView and the adapter
        listView.setAdapter(itemsAdapter);
    }

    private void setupListViewListener() {

        // delete an item when long clicked
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //Register a callback to be invoked when an item in this AdapterView has been clicked.
            // AdapterView.OnItemLongClickListener() is a callback interface
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId)    //callback method
            {
                final Long id = (Long) dataRows.keySet().toArray()[position];   //get item id
                Log.i("MainActivity", "Long Clicked item " + dataRows.keySet().toArray()[position]);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);   // this refers to the anonymous class; MainActivity.this refers to the MainActivity Context
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.dialog_delete_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //find id of delete position
                                deleteItemFromDatabase(id); // delete item from database
                                listItems.remove(position);// Remove item from the ArrayList
                                dataRows.remove(id); // Remove item from tree
                                itemsAdapter.notifyDataSetChanged();// Notify listView adapter to update list
                            }
                        })
                        .setNegativeButton(R.string.dialog_delete_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();    //create and display Alert Dialog
                return true;
            }
        });

        // go to edit view when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  //params of click event
                Long updateItemId = (Long) dataRows.keySet().toArray()[position];
                String updateItemName = listItems.get(position).get("Item Name");
                String updateItemTime = listItems.get(position).get("Item Time");
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItemName);

                Intent intent = new Intent(MainActivity.this, EditAddItemActivity.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("updateItemName", updateItemName);
                    intent.putExtra("updateItemTime", updateItemTime);
                    intent.putExtra("updateItemId", updateItemId);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                    itemsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    // on returning to MainActivity update listView and dataRows
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Long resultItemID = data.getLongExtra("resultItemID", -1);
            String resultItemName = data.getStringExtra("resultItemName");
            String resultItemTime = data.getStringExtra("resultItemTime");

            Log.i("Update View", "" + resultItemName + " " + resultItemTime);


            if (requestCode == ADD_ITEM_REQUEST_CODE) {


            } else if (requestCode == EDIT_ITEM_REQUEST_CODE) {   //remove old itemView
                int position = new ArrayList<>(dataRows.keySet()).indexOf(resultItemID);   //get item id
                listItems.remove(position);// Remove item from the ArrayList
                dataRows.remove(resultItemID); // Remove item from map
                itemsAdapter.notifyDataSetChanged();// Notify listView adapter to update list
            }

            // add item to dataRows and listView
            LinkedHashMap<String, String> newMap = new LinkedHashMap<>();
            newMap.put("Item Name", resultItemName);
            newMap.put("Item Time", resultItemTime);
            ArrayList<String> newRowValue = new ArrayList<>();
            newRowValue.add(resultItemName);
            newRowValue.add(resultItemTime);
            listItems.add(0, newMap);   // newly saved item always on the top
            LinkedHashMap<Long, ArrayList<String>> copiedMap = new LinkedHashMap<>(dataRows); //clone old dataRows
            dataRows.clear();
            dataRows.putAll(copiedMap);
            dataRows.put(resultItemID, newRowValue);
        }
    }

    private void readItemsFromDatabase() {
        try {
            new readItemsFromDatabase(this, toDoItemDao).execute().get();
        } catch (Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }

    private void deleteItemFromDatabase(Long id) {
        new deleteItemFromDatabase(this, toDoItemDao, id).execute();
    }

    private static class readItemsFromDatabase extends AsyncTask<Void, Void, Void> {
        //Use asynchronous task to run query on the background and wait for result

        private final WeakReference<MainActivity> mActivityRef;

        private ToDoItemDao toDoItemDao;

        // Constructor
        readItemsFromDatabase(MainActivity activity, ToDoItemDao dao) {
            mActivityRef = new WeakReference<>(activity);   // weak reference to prevent memory leak
            toDoItemDao = dao;
        }

        @Override
        // Read items from database
        protected Void doInBackground(Void... voids) { // ... == vargars == Variable Arguments
            MainActivity mainActivity = mActivityRef.get();

            List<ToDoItem> itemsFromDB = toDoItemDao.listAllDesc();
            mainActivity.dataRows = new LinkedHashMap<>();

            if (itemsFromDB != null & itemsFromDB.size() > 0) {
                for (ToDoItem item : itemsFromDB) {
                    ArrayList<String> dataRowValue = new ArrayList<>();
                    dataRowValue.add(item.getToDoItemName());
                    dataRowValue.add(item.getToDoItemTime());
                    mainActivity.dataRows.put(item.getToDoItemID(), dataRowValue);
                    Log.i("SQLite read item", "Name: " + item.getToDoItemName() + " Time: " + item.getToDoItemTime());
                }
            }
            return null;
        }
    }

    private static class deleteItemFromDatabase extends AsyncTask<Void, Void, Void> {
        //Use asynchronous task to run query on the background and wait for result

        private final WeakReference<MainActivity> mActivityRef;
        private ToDoItemDao toDoItemDao;
        Long id;

        // Constructor
        deleteItemFromDatabase(MainActivity activity, ToDoItemDao dao, Long id) {
            mActivityRef = new WeakReference<>(activity);
            toDoItemDao = dao;
            this.id = id;
        }

        @Override
        // delete item by id
        protected Void doInBackground(Void... voids) { // ... == vargars == Variable Arguments
            toDoItemDao.deleteItem(id);
            return null;
        }
    }
}
