package com.virid.ViridDirectory;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main);		
		super.onCreate(savedInstanceState);
	
		searchText = (EditText) findViewById(R.id.searchText);
		db = (new DatabaseHelper(this)).getWritableDatabase();


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
						"SELECT _id, firstName, lastName, title, \" - \" || department as department FROM employee WHERE firstName || ' ' || lastName LIKE ?",
						new String[] { "%" + searchText.getText().toString()
								+ "%" });
		adapter = new SimpleCursorAdapter(this, R.layout.employee_list_item,
				cursor, new String[] { "firstName", "lastName", "title", "department" },
				new int[] { R.id.firstName, R.id.lastName, R.id.title, R.id.department });
		setListAdapter(adapter);
	}
	
}