package com.squirrel.addressbook;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by squirrel on 12/6/15.
 */
public class AddActivity extends AppCompatActivity {

    private final String LOG_TAG = AddActivity.class.getSimpleName();
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

        mContentResolver = AddActivity.this.getContentResolver();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataIsValid()){
                    ContentValues values = new ContentValues();
                    values.put(ContactsContract.ContactsColumns.CONTACT_NAME, mNameTextView.getText().toString());
                    values.put(ContactsContract.ContactsColumns.CONTACT_EMAIL, mEmailTextView.getText().toString());
                    values.put(ContactsContract.ContactsColumns.CONTACT_PHONE, mPhoneTextView.getText().toString());
                    values.put(ContactsContract.ContactsColumns.CONTACT_ADDRESS, mAddressTextView.getText().toString());

                    Uri uri = mContentResolver.insert(ContactsContract.URI_TABLE, values);
                    Log.d(LOG_TAG, "record id is" + uri.toString());
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Data is invalid", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * check if data which user filled is valid
     * @return true if data is valid, false - not valid
     */
    private boolean dataIsValid(){
        if(mNameTextView.getText().toString().length() == 0 || mEmailTextView.getText().toString().length() == 0
                || mPhoneTextView.getText().toString().length() == 0
                || mAddressTextView.getText().toString().length() == 0){
            return false;
        } else {
            return true;
        }

    }

    private boolean dataIsEntered(){
        if(mNameTextView.getText().toString().length() > 0 || mEmailTextView.getText().toString().length() > 0
                || mPhoneTextView.getText().toString().length() > 0
                || mAddressTextView.getText().toString().length() > 0){
            return true;
        } else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if(dataIsEntered()){
            ContactsDialog contactsDialog = new ContactsDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ContactsDialog.DIALOG_TYPE, ContactsDialog.EXIT_CONFIRMATION);
            contactsDialog.setArguments(bundle);
            contactsDialog.show(getSupportFragmentManager(),"exit-confirmation");
        } else {
            super.onBackPressed();
        }
    }
}
