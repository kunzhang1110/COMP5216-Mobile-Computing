package comp5216.sydney.edu.au.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class EditToDoItemActivity extends Activity {
	public int position=0;
	EditText etItem; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//populate the screen using the layout
		setContentView(R.layout.activity_edit_item);
		
		//Get the data from the main screen
		String editItem = getIntent().getStringExtra("item");
		position = getIntent().getIntExtra("position",-1);
		
		// show original content in the text field
		etItem = (EditText)findViewById(R.id.etEditItem);
		etItem.setText(editItem);
	}

	public void onSubmit(View v) {
	  etItem = (EditText) findViewById(R.id.etEditItem);
	  
	  // Prepare data intent for sending it back
	  Intent data = new Intent();
	  
	  // Pass relevant data back as a result
	  data.putExtra("item", etItem.getText().toString());
	  data.putExtra("position", position);


	  // Activity finished ok, return the data
	  setResult(RESULT_OK, data); // set result code and bundle data for response
	  finish(); // closes the activity, pass data to parent
	} 
}
