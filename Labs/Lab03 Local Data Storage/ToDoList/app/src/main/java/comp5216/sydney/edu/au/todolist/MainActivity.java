package comp5216.sydney.edu.au.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    // Define variables
    ListView listView;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    EditText addItemEditText;
    public final int EDIT_ITEM_REQUEST_CODE = 647;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use "activity_main.xml" as layout
        setContentView(R.layout.activity_main); //R => Resources => a class representing information in res directory

        // Reference the "listView" variable to the id "lstView" in the layout
        listView = (ListView) findViewById(R.id.listView);
        addItemEditText = (EditText) findViewById(R.id.txtNewItem);

        // Create an ArrayList of String
        items = new ArrayList<String>();
        items.add("Study 5216");
        items.add("Study 5619");

        // Create an adapter for the list view using Android's built-in item layout
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        // Connect the listView and the adapter
        listView.setAdapter(itemsAdapter);

        // Setup ListViewListener
        setupListViewListener();
    }

    public void onAddItemClick(View view){
        String toAddString = addItemEditText.getText().toString();  //get input from addItemEdiText
        if (toAddString != null && toAddString.length() > 0){
            //get current time with format
            Date currentTime = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("MMM d HH:mm");
            //to convert Date to String, use format method of SimpleDateFormat class.
            String strDate = dateFormat.format(currentTime);

          //  System.out.println("converted Date to String: " + strDate);
            toAddString = strDate + "    " + toAddString;
            itemsAdapter.add(toAddString);  // Add text to list view adapter
            addItemEditText.setText("");    // clear addItemEditText
        }
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
            }
        }

    }

}

