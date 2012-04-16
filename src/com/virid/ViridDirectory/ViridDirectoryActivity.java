package com.virid.ViridDirectory;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ViridDirectoryActivity extends ListActivity {

	protected EditText searchText;
	protected SQLiteDatabase db;
	protected Cursor cursor;
	protected ListAdapter adapter;
	protected Dialog mSplashDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		MyStateSaver data = (MyStateSaver) getLastNonConfigurationInstance();
	    if (data != null) {
	        // Show splash screen if still loading
	        if (data.showSplashScreen) {
	            showSplashScreen();
	        }
	        setContentView(R.layout.main);   
	        searchText = (EditText) findViewById(R.id.searchText);
			db = (new DatabaseHelper(this)).getWritableDatabase();
	 
	        // Rebuild your UI with your saved state here
	    } else {
	        showSplashScreen();
	        setContentView(R.layout.main);
	        searchText = (EditText) findViewById(R.id.searchText);
			db = (new DatabaseHelper(this)).getWritableDatabase();
	        // Do your heavy loading here on a background thread
	    }
		
		
		
		//setContentView(R.layout.main);		
		
	
//		searchText = (EditText) findViewById(R.id.searchText);
//		db = (new DatabaseHelper(this)).getWritableDatabase();


	}

	public void onListItemClick(ListView parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, EmployeeDetails.class);
		Cursor cursor = (Cursor) adapter.getItem(position);
		intent.putExtra("EMPLOYEE_ID",
				cursor.getInt(cursor.getColumnIndex("_id")));
		startActivity(intent);
	}

	public void search(View view) {
		// || is the concatenation operation in SQLite
		cursor = db
				.rawQuery(
						"SELECT _id, firstName, lastName, title, \" - \" || department as department FROM viridEmployee WHERE firstName || ' ' || lastName LIKE ?",
						new String[] { "%" + searchText.getText().toString()
								+ "%" });
		adapter = new SimpleCursorAdapter(this, R.layout.employee_list_item,
				cursor, new String[] { "firstName", "lastName", "title", "department" },
				new int[] { R.id.firstName, R.id.lastName, R.id.title, R.id.department });
		setListAdapter(adapter);
	}
	
	
	
	
	@Override
	public Object onRetainNonConfigurationInstance() {
	    MyStateSaver data = new MyStateSaver();
	    // Save your important data here
	 
	    if (mSplashDialog != null) {
	        data.showSplashScreen = true;
	        removeSplashScreen();
	    }
	    return data;
	}
	 
	/**
	 * Removes the Dialog that displays the splash screen
	 */
	protected void removeSplashScreen() {
	    if (mSplashDialog != null) {
	        mSplashDialog.dismiss();
	        mSplashDialog = null;
	    }
	}
	 
	/**
	 * Shows the splash screen over the full Activity
	 */
	protected void showSplashScreen() {
	    mSplashDialog = new Dialog(this, R.style.SplashScreen);
	    mSplashDialog.setContentView(R.layout.splash);
	    mSplashDialog.setCancelable(false);
	    mSplashDialog.show();
	    
	    TextView splashText = (TextView) mSplashDialog.findViewById(R.id.splashText);
		splashText.setText(Html.fromHtml(getString(R.string.splashText)));
	 
	    // Set Runnable to remove splash screen just in case
	    final Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	      public void run() {
	        removeSplashScreen();
	      }
	    }, 3000);
	}
	 
	/**
	 * Simple class for storing important data across config changes
	 */
	private class MyStateSaver {
	    public boolean showSplashScreen = false;
	    // Your other important fields here
	}

	
	
	
	
}