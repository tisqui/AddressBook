package com.squirrel.addressbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by squirrel on 12/6/15.
 */
public class ContactsDialog extends DialogFragment {
    private static final String LOG_TAG = ContactsDialog.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    public static final String DIALOG_TYPE="command";
    public static final String DELETE_CONTACT = "deleteRecord";
    public static final String DELETE_DB = "deleteDB";
    public static final String EXIT_CONFIRMATION = "exitConfirmation";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        mLayoutInflater = getActivity().getLayoutInflater();
        final View view = mLayoutInflater.inflate(R.layout.dialog, null);
        final String command = getArguments().getString(DIALOG_TYPE);
        if(command.equals(DELETE_CONTACT)){
            final int id = getArguments().getInt(ContactsContract.ContactsColumns.CONTACTS_ID);
            String name = getArguments().getString(ContactsContract.ContactsColumns.CONTACT_NAME);
            TextView message = (TextView) view.findViewById(R.id.dialog_message);
            message.setText("Are you sure you want to delete " + name + " from contacts?");
            alertDialog.setView(view).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Uri uri = ContactsContract.Contacts.buildContactUri(String.valueOf(id));
                    contentResolver.delete(uri, null, null);
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //make sure all opened activities are cleared
                    startActivity(intent);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } else if(command.equals(DELETE_DB)){
            TextView message = (TextView) view.findViewById(R.id.dialog_message);
            message.setText("AHTUNG!!!! Are you sure you want to delete database?");
            alertDialog.setView(view).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Uri uri = ContactsContract.URI_TABLE;
                    contentResolver.delete(uri, null, null);
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //make sure all opened activities are cleared
                    startActivity(intent);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } else if(command.equals(EXIT_CONFIRMATION)){
            TextView message = (TextView) view.findViewById(R.id.dialog_message);
            message.setText("AHTUNG!!!! Are you sure you want to exit without saving?");
            alertDialog.setView(view).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

        } else {
            Log.d(LOG_TAG, "Incorrect command sent to the dialog");
        }

        return alertDialog.create();
    }

}
