package com.squirrel.addressbook;

import android.support.v4.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by squirrel on 12/5/15.
 */
public class ContactsListLoader extends AsyncTaskLoader<List<Contact>> {

    private List<Contact> mContacts;
    private static final String LOG_TAG = ContactsListLoader.class.getSimpleName();
    private ContentResolver mContentResolver;
    private Cursor mCursor;

    public ContactsListLoader(Context context, Uri uri, ContentResolver contentResolver) {
        super(context);
        mContentResolver = contentResolver;
    }

    @Override
    public List<Contact> loadInBackground() {
        String[] projection = {BaseColumns._ID, ContactsContract.ContactsColumns.CONTACT_NAME,
                ContactsContract.ContactsColumns.CONTACT_EMAIL,
                ContactsContract.ContactsColumns.CONTACT_PHONE,
                ContactsContract.ContactsColumns.CONTACT_ADDRESS
        };
        List<Contact> listOfContacts = new ArrayList<Contact>();

        mCursor = mContentResolver.query(ContactsContract.URI_TABLE, projection, null, null, null);
        if( mCursor != null){
            if(mCursor.moveToFirst()){
                do{
                    int id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.ContactsColumns.CONTACT_NAME));
                    String email = mCursor.getString(mCursor.getColumnIndex(ContactsContract.ContactsColumns.CONTACT_EMAIL));
                    String phone = mCursor.getString(mCursor.getColumnIndex(ContactsContract.ContactsColumns.CONTACT_PHONE));
                    String address = mCursor.getString(mCursor.getColumnIndex(ContactsContract.ContactsColumns.CONTACT_ADDRESS));
                    Contact contact = new Contact(id, name, email, phone, address);
                    listOfContacts.add(contact);
                } while (mCursor.moveToNext());
            }
        }
        return listOfContacts;
    }

    @Override
    public void deliverResult(List<Contact> data) {
        //check if data was reset
        if(isReset()){
            //if no data was passed, just close
            if(data != null){
                mCursor.close();
            }
        }
        List<Contact> oldListOfContacts = mContacts;
        if(mContacts == null || mContacts.size() == 0){
            Log.d(LOG_TAG, "There is no data returned!!!");
        }
        mContacts = data;
        if(isStarted()){
            super.deliverResult(data);
        }
        //the new data is different from the previous one
        if(oldListOfContacts != null && oldListOfContacts != data){
            mCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if(mContacts != null){
            //data is set and processed
            deliverResult(mContacts);
        }

        if(takeContentChanged() || mContacts == null){
            //if the data is changed when loader was stopped
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        //loader is stopping to load the data
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(mCursor != null){
            mCursor.close();
        }
        mContacts = null; //so next time on start we will load data again
    }

    @Override
    public void onCanceled(List<Contact> data) {
        super.onCanceled(data);
        if(mCursor != null){
            mCursor.close();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }
}
