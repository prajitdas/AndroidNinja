package com.prajitdas.contactmanager;

import com.prajitdas.contactmanager.FakeContract;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ContactManager extends Activity {
	
    public static final String TAG = "ContactManagerByPrajit";

    private ListView mContactListView;
    private Button mAddAccountButton;
    private boolean mShowInvisible;
    private CheckBox mShowInvisibleControl;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_manager);
		
        // Obtain handles to UI objects
        mAddAccountButton = (Button) findViewById(R.id.addContactButton);
        mShowInvisibleControl = (CheckBox) findViewById(R.id.showInvisible);
		mContactListView = (ListView) findViewById(R.id.contactListView);

        // Initialize class properties
        mShowInvisible = false;
        mShowInvisibleControl.setChecked(mShowInvisible);

        // Register handler for UI elements
        mAddAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "mAddAccountButton clicked");
                launchContactAdder();
            }
        });
        mShowInvisibleControl.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "mShowInvisibleControl changed: " + isChecked);
                mShowInvisible = isChecked;
                populateContactList();
            }
        });

        // Populate the contact list
        populateContactList();
	}
	
    /**
     * Populate the contact list based on account currently selected in the account spinner.
     */
    private void populateContactList() {
    	setFakeContacts();
//    	setRealContacts();
//    	showAllBirthdays();
    }

    /**
     * Obtains the contact list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
	@SuppressWarnings("unused")
	private void setRealContacts()
    {
        // Run query
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
                (mShowInvisible ? "0" : "1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        // Build adapter with contact entries with real contacts
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME
        };
        @SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_entry, cursor,
                fields, new int[] {R.id.contactEntryText});
        mContactListView.setAdapter(adapter);
    }

    /**
     * Obtains the contact list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
	private void setFakeContacts()
    {
        // Run query
        Uri uri = FakeContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                FakeContract.Contacts._ID,
                FakeContract.Contacts.DISPLAY_NAME
        };
        String selection = FakeContract.Contacts.IN_VISIBLE_GROUP + " = '" +
                (mShowInvisible ? "0" : "1") + "'";
        String[] selectionArgs = null;
        String sortOrder = FakeContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        // Build adapter with contact entries with fake contacts
//        Cursor cursor = managedQuery(uri, projection, selection, selectionArgs, sortOrder);
        Log.e("PKD", uri.toString());
		Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        String[] fields = new String[] {
        		FakeContract.Data.DISPLAY_NAME
        };
        @SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_entry, cursor,
        		fields, new int[] {R.id.contactEntryText});
        mContactListView.setAdapter(adapter);
    }

    public void showAllBirthdays() {
		// Show all the birthdays sorted by friend's name
		String URL = "content://com.prajitdas.provider.Birthday/friends";
		Uri friends = Uri.parse(URL);
		Cursor c = getContentResolver().query(friends, null, null, null, "name");
		String result = "Javacodegeeks Results:";
		if (!c.moveToFirst()) {
			Toast.makeText(this, result+" no content yet!", Toast.LENGTH_LONG).show();
		} 
		else {
			do {
				result = result + "\n" + c.getString(c.getColumnIndex("name")) + 
						" with id " +  c.getString(c.getColumnIndex("id")) + 
						" has birthday: " + c.getString(c.getColumnIndex("birthday"));
			} while (c.moveToNext());
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		}
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    /**
     * Launches the ContactAdder activity to add a new contact to the selected accont.
     */
    protected void launchContactAdder() {
        Intent i = new Intent(this, ContactAdder.class);
        startActivity(i);
    }
}