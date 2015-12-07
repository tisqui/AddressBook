package com.squirrel.addressbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by squirrel on 12/5/15.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {
    private LayoutInflater mLayoutInflater;
    private static FragmentManager sFragmentManager;

    public ContactsAdapter(Context context, FragmentManager fragmentManager){
        super(context, android.R.layout.simple_list_item_2);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sFragmentManager = fragmentManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.contact, parent, false);
        } else {
            view = convertView;
        }
        final Contact contact = getItem(position);
        final int id = contact.getId();
        final String name = contact.getName();
        final String email = contact.getEmail();
        final String phone = contact.getPhone();
        final String address = contact.getAddress();

        ((TextView) view.findViewById(R.id.contact_name)).setText(name);
        ((TextView) view.findViewById(R.id.contact_email)).setText(email);
        ((TextView) view.findViewById(R.id.contact_phone)).setText(phone);
        ((TextView) view.findViewById(R.id.contact_address)).setText(address);

        Button editBtn = (Button) view.findViewById(R.id.edit_button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to the edit activity
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra(ContactsContract.ContactsColumns.CONTACTS_ID, String.valueOf(id));
                intent.putExtra(ContactsContract.ContactsColumns.CONTACT_NAME, name);
                intent.putExtra(ContactsContract.ContactsColumns.CONTACT_EMAIL, email);
                intent.putExtra(ContactsContract.ContactsColumns.CONTACT_PHONE, phone);
                intent.putExtra(ContactsContract.ContactsColumns.CONTACT_ADDRESS, address);

                getContext().startActivity(intent);

            }
        });

        Button deleteBtn = (Button) view.findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsDialog dialog = new ContactsDialog();
                Bundle args = new Bundle();
                args.putString(ContactsDialog.DIALOG_TYPE, ContactsDialog.DELETE_CONTACT);
                args.putInt(ContactsContract.ContactsColumns.CONTACTS_ID, id);
                args.putString(ContactsContract.ContactsColumns.CONTACT_NAME, name);
                dialog.setArguments(args);
                dialog.show(sFragmentManager, "delete-contact");
            }
        });

        return view;
    }

    public void setData(List<Contact> contacts){
        clear();
        if(contacts != null){
            for(Contact contact : contacts){
                add(contact);
            }
        }
    }

}
