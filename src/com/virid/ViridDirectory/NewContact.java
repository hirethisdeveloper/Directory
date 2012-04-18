package com.virid.ViridDirectory;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewContact extends Activity {

	private static final String EMPTY_STRING = "";
	private Button NewContact_BTNSave;
	private Spinner NewContact_Department;

	private EditText NewContact_FirstName;
	private EditText NewContact_LastName;
	private EditText NewContact_Title;
	private EditText NewContact_OfficePhone;
	private EditText NewContact_OfficePhoneExt;
	private EditText NewContact_CellPhone;
	private EditText NewContact_Email;
	private EditText NewContact_AIM;
	private EditText NewContact_MSN;
	private CheckBox NewContact_isManager;
	private String defaultOfficePhone = "703.689.2121";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_contact);
	}

	// NewContact_BTNSave onClick handler
	public void newContactSave(View button) {
		this.findAllViewsById();

		String firstName = NewContact_FirstName.getText().toString();
		String lastName = NewContact_LastName.getText().toString();
		String title = NewContact_Title.getText().toString();
		String officePhone = NewContact_OfficePhone.getText().toString();
		String officePhoneExt = NewContact_OfficePhoneExt.getText().toString();
		String cellPhone = NewContact_CellPhone.getText().toString();
		String email = NewContact_Email.getText().toString();
		String aim = NewContact_AIM.getText().toString();
		String msn = NewContact_MSN.getText().toString();
		boolean isManager = NewContact_isManager.isChecked();
		String department = NewContact_Department.getSelectedItem().toString();

		if (officePhone.length() < 2) { officePhone = defaultOfficePhone; }
		
		// check to see if user exists
		int employeeId = 0;

		SQLiteDatabase db = (new DatabaseHelper(this)).getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT _id FROM viridEmployee WHERE firstName = ? and lastName = ?",
						new String[] { firstName, lastName });

		// USER EXISTS SHOW MESSAGE
		if (cursor.getCount() == 1) {
			alert("User already exists.", Toast.LENGTH_LONG);
		}

		// USER NOT FOUND, PROCEED TO CREATE RECORD
		else {

			int isManagerInt = 0;
			if (isManager == true) { isManagerInt = 1; }
			
			
			String sql = "INSERT INTO viridEmployee (firstName, lastName, title, department, officePhone, isManager, managerId, officePhoneExt, cellPhone, email, aim, msn) VALUES ('"
					+ firstName
					+ "', '"
					+ lastName
					+ "', '"
					+ title
					+ "', '"
					+ department
					+ "', '"
					+ isManagerInt
					+ "', '0', '"
					+ officePhone
					+ "', '"
					+ officePhoneExt
					+ "', '"
					+ cellPhone
					+ "', '"
					+ email
					+ "', '"
					+ aim
					+ "', '"
					+ msn + "')";
			db.execSQL(sql);

			if (wasAdded(firstName, lastName)) {
				alert("User added.", Toast.LENGTH_LONG);
			} else {
				alert("Error adding user.", Toast.LENGTH_LONG);
			}
		}

		// add to database
	}

	public void alert(String message, int length) {
		Toast.makeText(getApplicationContext(), message, length).show();
	}

	public boolean wasAdded(String firstName, String lastName) {
		SQLiteDatabase db = (new DatabaseHelper(this)).getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT _id FROM viridEmployee WHERE firstName = ? and lastName = ?",
						new String[] { firstName, lastName });
		if (cursor.getCount() > 0) {
			return true;
		}

		return false;
	}

	private void findAllViewsById() {
		NewContact_FirstName = (EditText) findViewById(R.id.NewContact_FirstName);
		NewContact_LastName = (EditText) findViewById(R.id.NewContact_LastName);
		NewContact_Title = (EditText) findViewById(R.id.NewContact_Title);
		NewContact_OfficePhone = (EditText) findViewById(R.id.NewContact_OfficePhone);
		NewContact_OfficePhoneExt = (EditText) findViewById(R.id.NewContact_OfficePhoneExt);
		NewContact_CellPhone = (EditText) findViewById(R.id.NewContact_CellPhone);
		NewContact_Email = (EditText) findViewById(R.id.NewContact_Email);
		NewContact_AIM = (EditText) findViewById(R.id.NewContact_AIM);
		NewContact_MSN = (EditText) findViewById(R.id.NewContact_MSN);
		NewContact_Department = (Spinner) findViewById(R.id.NewContact_Department);
		NewContact_isManager = (CheckBox) findViewById(R.id.NewContact_isManager);
		NewContact_BTNSave = (Button) findViewById(R.id.NewContact_BTNSave);
	}

}
