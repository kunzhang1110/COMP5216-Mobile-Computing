package au.edu.sydney.comp5216.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

public class EditAddItemActivity extends AppCompatActivity {
    EditText editTextItem;
    TextView timeTextView;
    ToDoItemDB db;
    ToDoItemDao toDoItemDao;
    Long existingItemId;
    String itemName;
    String itemTime;
    Boolean updateFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add);

        // initialize variables
        editTextItem = (EditText) findViewById(R.id.editTextItem);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        db = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        toDoItemDao = db.toDoItemDao();
        updateFlag = false;
        existingItemId = null;

        // get data from main activity
        if (getIntent().getExtras() != null) {
            String existingItemName = getIntent().getStringExtra("updateItemName");
            String existingItemTime = getIntent().getStringExtra("updateItemTime");
            existingItemId = getIntent().getLongExtra("updateItemId", -1);
            editTextItem.setText(existingItemName);
            timeTextView.setText("Last Edit:" + existingItemTime);
            updateFlag = true;
            Log.i("getIntent", "intent" + existingItemId);
        }
    }

    // Button Save Click Action
    public void onSave(View view) {
        itemName = editTextItem.getText().toString();  //get input from addItemEdiText
        if (itemName.length() > 0) {
            Date currentTime = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            itemTime = dateFormat.format(currentTime);
            saveItemToDatabase();
            editTextItem.setText("");    // clear addItemEditText

            Intent data = new Intent();

            // Pass relevant data back as a result
            data.putExtra("resultItemID", existingItemId);
            data.putExtra("resultItemName", itemName);
            data.putExtra("resultItemTime", itemTime);

            // Activity finished ok, return the data
            setResult(RESULT_OK, data); // set result code and bundle data for response
            finish(); // closes the activity, pass data to parent
        }

    }

    // Button CancelF Click Action
    public void onCancel(View view) {
        itemName = editTextItem.getText().toString();  //get input from addItemEdiText
        if (itemName != null && itemName.length() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditAddItemActivity.this);   // this refers to the anonymous class; MainActivity.this refers to the MainActivity Context
            builder.setTitle(R.string.dialog_discard_title)
                    .setMessage(R.string.dialog_diescard_msg)
                    .setPositiveButton(R.string.dialog_diescard_confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editTextItem.setText("");
                            Intent data = new Intent();
                            setResult(RESULT_CANCELED, data);
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.dialog_diescard_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            builder.create().show();    //create and display Alert Dialog
        } else {
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
        }
    }

    private void saveItemToDatabase() {
        new saveItemToDatabase(this, toDoItemDao).execute();
    }

    private static class saveItemToDatabase extends AsyncTask<Void, Void, Void> {

        private final WeakReference<EditAddItemActivity> eActivityRef;
        private ToDoItemDao toDoItemDao;

        private String itemName;
        private String itemTime;


        saveItemToDatabase(EditAddItemActivity activity, ToDoItemDao dao) {
            eActivityRef = new WeakReference<>(activity);
            toDoItemDao = dao;

        }

        protected Void doInBackground(Void... voids) {
            EditAddItemActivity editAddItemActivity = eActivityRef.get();
            itemName = editAddItemActivity.itemName;
            itemTime = editAddItemActivity.itemTime;
            ToDoItem item = new ToDoItem(itemName, itemTime);
            if (editAddItemActivity.updateFlag) {
                item.setToDoItemID(editAddItemActivity.existingItemId);
                toDoItemDao.update(item);
            } else {
                editAddItemActivity.existingItemId = toDoItemDao.insert(item);
                Log.i("SAVE", "" + editAddItemActivity.existingItemId);
            }
            Log.i("SQLite saved item", item.getToDoItemName());
            return null;
        }
    }

}