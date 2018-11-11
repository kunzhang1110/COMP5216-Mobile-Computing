package comp5216.sydney.edu.au.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

public class MainActivity extends AppCompatActivity {

    // Define variables
    ListView listView;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    EditText addItemEditText;
    ToDoItemDB db;
    ToDoItemDao toDoItemDao;

    public final int EDIT_ITEM_REQUEST_CODE = 647;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use "activity_main.xml" as the layout
        setContentView(R.layout.activity_main);

        // Reference the "listView" variable to the id "lstView" in the layout
        listView = (ListView) findViewById(R.id.lstView);
        addItemEditText = (EditText) findViewById(R.id.txtNewItem);

        // Create an ArrayList of String - not required if calling readItemsFromDatabase next
        //items = new ArrayList<String>();
        //items.add("item one");
        //items.add("item two");

        // Must call it before creating the adapter, because it references the right item list
        //readItemsFromFile();

        // Create an instance of ToDoItemDB and Dao
        db = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        toDoItemDao = db.toDoItemDao();
        readItemsFromDatabase();

        // Create an adapter for the list view using Android's built-in item layout
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        // Connect the listView and the adapter
        listView.setAdapter(itemsAdapter);

        // Setup listView listeners
        setupListViewListener();
    }

    public void onAddItemClick(View view) {
        String toAddString = addItemEditText.getText().toString();
        if (toAddString != null && toAddString.length() > 0) {
            itemsAdapter.add(toAddString); // Add text to list view adapter
            addItemEditText.setText("");
            //saveItemsToFile();
            saveItemsToDatabase();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                String editedItem = data.getExtras().getString("item");
                int position = data.getIntExtra("position", -1);
                items.set(position, editedItem);
                Log.i("Updated Item in list:", editedItem + ",position:"
                        + position);

                Toast.makeText(this, "updated:" + editedItem, Toast.LENGTH_SHORT).show();
                itemsAdapter.notifyDataSetChanged();

                //saveItemsToFile();
                saveItemsToDatabase();
            }
        }
    }

    private void setupListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId)
            {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                items.remove(position); // Remove item from the ArrayList
                                itemsAdapter.notifyDataSetChanged(); // Notify listView adapter to update the list

                                //saveItemsToFile();
                                saveItemsToDatabase();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });

                builder.create().show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String updateItem = (String) itemsAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItem);

                Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("item", updateItem);
                    intent.putExtra("position", position);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                    itemsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void readItemsFromFile(){
        //retrieve the app's private folder.
        //this folder cannot be accessed by other apps
        File filesDir = getFilesDir();
        //prepare a file to read the data
        File todoFile = new File(filesDir,"todo.txt");
        //if file does not exist, create an empty list
        if(!todoFile.exists()){
            items = new ArrayList<String>();
        }else{
            try{
                //read data and put it into the ArrayList
                items = new ArrayList<String>(FileUtils.readLines(todoFile, "UTF-8"));
            }
            catch(IOException ex){
                items = new ArrayList<String>();
            }
        }
    }

    private void saveItemsToFile(){
        File filesDir = getFilesDir();
        //using the same file for reading. Should use define a global string instead.
        File todoFile = new File(filesDir,"todo.txt");
        try{
            //write list to file
            FileUtils.writeLines(todoFile,items);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private void readItemsFromDatabase()
    {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //read items from database
                    List<ToDoItem> itemsFromDB = toDoItemDao.listAll();
                    items = new ArrayList<String>();
                    if (itemsFromDB != null & itemsFromDB.size() > 0) {
                        for (ToDoItem item : itemsFromDB) {
                            items.add(item.getToDoItemName());
                            Log.i("SQLite read item", "ID: " + item.getToDoItemID() + " Name: " + item.getToDoItemName());
                        }
                    }
                    return null;
                }
            }.execute().get();
        }
        catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }

    private void saveItemsToDatabase()
    {
        //Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //delete all items and re-insert
                toDoItemDao.deleteAll();
                for (String todo : items) {
                    ToDoItem item = new ToDoItem(todo);
                    toDoItemDao.insert(item);
                    Log.i("SQLite saved item", todo);
                }
                return null;
            }
        }.execute();
    }
}
