package com.squirrel.addressbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by squirrel on 12/6/15.
 */
public class ContactsDialog extends DialogFragment {
    private LayoutInflater mLayoutInflater;
    public static final String DIALOG_TYPE="command";
    public static final String DELETE_CONTACT = "delete";

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
            });
        }
        //TODO other types of dialogs
        return alertDialog.create();
    }

}
