package au.edu.sydney.comp5216.todolistazure;
// The Azure Database Table name must be todolist wiht fields：id; toDoItemID; toDoItemName;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncTable;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    MobileServiceTable<ToDoItem> mToDoTable;
    MobileServiceSyncTable<ToDoItem> mToDoTableLocal;
    ArrayList<String> items;
    MobileServiceClient mClient;
    ListView listView;
    ArrayAdapter<String> itemsAdapter;
    EditText addItemEditText;
    public final int EDIT_ITEM_REQUEST_CODE = 647;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AzureServiceAdapter.Initialize(this);

        // Reference the "listView" variable to the id "lstView" in the layout
        listView = (ListView) findViewById(R.id.listView);
        addItemEditText = (EditText) findViewById(R.id.txtNewItem);

        // get client and table
        mClient = AzureServiceAdapter.getInstance().getClient();
        mToDoTable = mClient.getTable("todolist",ToDoItem.class);


        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        readItemsFromDatabase();

        // Connect the listView and the adapter


//         Setup ListViewListener
        setupListViewListener();

    }

    private void setupListViewListener() {
        // delete an item when long clicked
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //Register a callback to be invoked when an item in this AdapterView has been clicked.
            // AdapterView.OnItemLongClickListener() is a callback interface
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId)    //callback method
            {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);   // this refers to the anonymous class; MainActivity.this refers to the MainActivity Context
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i){
                                items.remove(position);// Remove item from the ArrayList
                                itemsAdapter.notifyDataSetChanged();// Notify listView adapter to update list
                                saveItemsToDatabase();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();    //create and display Alert Dialog
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  //params of click event
                String updateItem = (String) itemsAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItem);

                Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
                if (intent != null){
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

    public void onAddItemClick(View view){
        String toAddString = addItemEditText.getText().toString();  //get input from addItemEdiText
        if (toAddString != null && toAddString.length() > 0){
            itemsAdapter.add(toAddString);  // Add text to list view adapter
            addItemEditText.setText("");    // clear addItemEditText
            saveItemsToDatabase();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                // Extract name value from result extras
                String editedItem = data.getExtras().getString("item");
                int position = data.getIntExtra("position", -1);
                items.set(position, editedItem);
                Log.i("Updated Item in list:", editedItem + ",position:" + position);
                Toast.makeText(this, "updated:" + editedItem, Toast.LENGTH_SHORT).show();
                itemsAdapter.notifyDataSetChanged();
                saveItemsToDatabase();
            }
        }
    }

    private void readItemsFromDatabase() {
        new readItemsFromDatabase(this).execute();
    }

    private void saveItemsToDatabase() {
        //Use asynchronous task to run query on the background to avoid locking UI
        new saveItemsToDatabase(this).execute();
    }

    private static class readItemsFromDatabase extends AsyncTask<Void, Void, List<ToDoItem>> {
        private final WeakReference<MainActivity> mActivityRef;
        MobileServiceTable<ToDoItem> mToDoTable;
        ArrayList<String> items;

        readItemsFromDatabase(MainActivity activity) {
            mActivityRef = new WeakReference<>(activity);
            MainActivity mainActivity = mActivityRef.get();
            this.mToDoTable = mainActivity.mToDoTable;
            this.items = mainActivity.items;
        }

        @Override
        protected List<ToDoItem> doInBackground(Void... voids) {
            Log.i("xxx","here");
            List<ToDoItem> itemsFromDB = null;
            try {
                itemsFromDB = mToDoTable.where().execute().get();
            } catch (Exception ex) {
                Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
            }
            Log.i("xxx","here");
            return itemsFromDB;
        }

        protected void onPostExecute(List<ToDoItem> result) {
            final List<ToDoItem> itemsFromDB = result;
            final MainActivity mainActivity = mActivityRef.get();
            if (itemsFromDB != null & itemsFromDB.size() > 0) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.itemsAdapter.clear();
                        for (ToDoItem item : itemsFromDB) {
                            Log.i("SQLite read item", "Name: " + item.getToDoItemName() + " ID: " + item.getId());
                            mActivityRef.get().itemsAdapter.add(item.getToDoItemName());
                        }
                    }
                });
            }
        }
    }

    private static class saveItemsToDatabase extends AsyncTask<Void, Void, Void>{

        private final WeakReference<MainActivity> mActivityRef;
        private ArrayList<String> items;
        MobileServiceTable<ToDoItem> mToDoTable;

        public saveItemsToDatabase(MainActivity activity){
            mActivityRef = new WeakReference<>(activity);
            MainActivity mainActivity = mActivityRef.get();
            this.mToDoTable = mainActivity.mToDoTable;
            items = mainActivity.items;
        }

        protected Void doInBackground(Void... voids) {
            //delete all items and re-insert


            for (String todo : items) {
                ToDoItem item = new ToDoItem(todo, todo);
                try {
                    mToDoTable.insert(item).get();
                } catch (Exception ex){
                    Log.e("doInBackground", ex.getStackTrace().toString());
                }
            }
            return null;
        }
    }

}
