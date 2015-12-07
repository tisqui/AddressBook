package com.squirrel.addressbook;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by squirrel on 12/5/15.
 */
public class EditActivity extends AppCompatActivity {

    private final String LOG_TAG = EditActivity.class.getSimpleName();
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mPhoneTextView;
    private TextView mAddressTextView;
    private Button mSaveButton;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mNameTextView = (TextView) findViewById(R.id.add_edit_activity_name);
        mEmailTextView = (TextView) findViewById(R.id.add_edit_activity_email);
        mPhoneTextView = (TextView) findViewById(R.id.add_edit_activity_phone);
        mAddressTextView = (TextView) findViewById(R.id.add_edit_activity_address);
        mSaveButton = (Button) findViewById(R.id.add_edit_activity_save_button);

        mContentResolver = EditActivity.this.getContentResolver();
        Intent intent = getIntent();
        final String id = intent.getStringExtra(ContactsContract.ContactsColumns.CONTACTS_ID);
        String name = intent.getStringExtra(ContactsContract.ContactsColumns.CONTACT_NAME);
        String email = intent.getStringExtra(ContactsContract.ContactsColumns.CONTACT_EMAIL);
        String phone = intent.getStringExtra(ContactsContract.ContactsColumns.CONTACT_PHONE);
        String address = intent.getStringExtra(ContactsContract.ContactsColumns.CONTACT_ADDRESS);

        mNameTextView.setText(name);
        mEmailTextView.setText(email);
        mPhoneTextView.setText(phone);
        mAddressTextView.setText(address);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put(ContactsContract.ContactsColumns.CONTACT_NAME, mNameTextView.getText().toString());
                    values.put(ContactsContract.ContactsColumns.CONTACT_EMAIL, mEmailTextView.getText().toString());
                    values.put(ContactsContract.ContactsColumns.CONTACT_PHONE, mPhoneTextView.getText().toString());
                    values.put(ContactsContract.ContactsColumns.CONTACT_ADDRESS, mAddressTextView.getText().toString());

                    Uri uri = ContactsContract.Contacts.buildContactUri(id);
                    int recordUpdated = mContentResolver.update(uri, values, null, null);

                    Log.d(LOG_TAG, "numberof records updated (should be 1): " + recordUpdated);
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                //if user process home - navigate back to the parent
                NavUtils.navigateUpFromSameTask(this);
                //TODO add validation
                break;
        }
        return true;
    }
}
