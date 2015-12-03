package com.squirrel.addressbook;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by squirrel on 12/2/15.
 * contains the definitions for the URI for the ContentProvider
 */
public class ContactsContract {

    interface ContactsColumns{
        String CONTACTS_ID = "id";
        String CONTACT_NAME = "contact_name";
        String CONTACT_EMAIL = "contact_email";
        String CONTACT_PHONE = "contact_phone";
        String CONTACT_ADDRESS = "contact_address";
    }

    public static final String AUTHORITY = "com.squirrel.addressbook.provider";
    public static final Uri BASE_URI= Uri.parse("content://" + AUTHORITY);
    private static final String PATH = "contacts";
    public static final Uri URI_TABLE = Uri.parse(BASE_URI.toString() + "/" + PATH);

    public static final String[] TO_LEVEL_PATHS = {PATH};

    public static class Contacts implements ContactsColumns, BaseColumns{
        //CONTENT_URI = BASE_URI+PATH
        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendEncodedPath(PATH).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".contacts";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + ".contacts";

        public static Uri buildContactUri(String contactId){
            return CONTENT_URI.buildUpon().appendEncodedPath(contactId).build();
        }

        public static String getContactId(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

}
