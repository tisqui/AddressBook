package com.squirrel.addressbook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by squirrel on 12/2/15.
 */
public class ContactsProvider extends ContentProvider {

    private ContactsDatabase mContactsDatabase;
    private static String LOG_TAG = ContactsProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //commands
    private static final int CONTACTS = 100;
    private static final int CONTACTS_ID= 101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ContactsContract.AUTHORITY;
        uriMatcher.addURI(authority, "contacts", CONTACTS);
        uriMatcher.addURI(authority, "contacts/*", CONTACTS_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mContactsDatabase = new ContactsDatabase(getContext());
        return true;
    }

    private void deleteDatabase(){
        mContactsDatabase.close();
        ContactsDatabase.deleteDatabase(getContext());
        mContactsDatabase = new ContactsDatabase(getContext());
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case CONTACTS:
                return ContactsContract.Contacts.CONTENT_TYPE;
            case CONTACTS_ID:
                return ContactsContract.Contacts.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Not valid Uri:" + uri);
        }
    }

    /**
     *
     * @param uri
     * @param projection list of columns you want to return
     * @param selection WHERE clause
     * @param selectionArgs arguments related to the selection
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
       //return the list of records
        final SQLiteDatabase database = mContactsDatabase.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ContactsDatabase.Tables.CONTACTS);

        switch (match){
            case CONTACTS:
                //id is not set
                break;
            case CONTACTS_ID:
                //only one item
                String id = ContactsContract.Contacts.getContactId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Not valid Uri:" + uri);

        }
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(LOG_TAG, "Insert uri=  " + uri + " values = " + values.toString());

        final SQLiteDatabase database = mContactsDatabase.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match){
            case CONTACTS:
                long recordId = database.insertOrThrow(ContactsDatabase.Tables.CONTACTS, null, values);
                return ContactsContract.Contacts.buildContactUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Not valid Uri:" + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(LOG_TAG, "Update uri=  " + uri);

        //in case we need to delete the whole db
        if(uri.equals(ContactsContract.URI_TABLE)){
           deleteDatabase();
            return 0;
        }

        final SQLiteDatabase database = mContactsDatabase.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match){
            case CONTACTS_ID:
                String id = ContactsContract.Contacts.getContactId(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                return database.delete(ContactsDatabase.Tables.CONTACTS, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException("Not valid Uri:" + uri);
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(LOG_TAG, "Update uri=  " + uri + " values = " + values.toString());

        final SQLiteDatabase database = mContactsDatabase.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String selectionCriteria = selection; //because value is changed in CONTACTS_ID case

        switch (match){
            case CONTACTS:
                break;
            case CONTACTS_ID:
                String id = ContactsContract.Contacts.getContactId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Not valid Uri:" + uri);
        }

        int updateCount = database.update(ContactsDatabase.Tables.CONTACTS, values, selectionCriteria, selectionArgs);

        return updateCount;
    }
}
