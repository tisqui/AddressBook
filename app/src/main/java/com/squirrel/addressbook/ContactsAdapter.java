package com.squirrel.addressbook;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by squirrel on 12/5/15.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {
    private static final String LOG_TAG = ContactsAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    private static FragmentManager sFragmentManager;
    private static final int DEFAULT_THRESHOLD = 200;

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

//        Button deleteBtn = (Button) view.findViewById(R.id.delete_button);
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ContactsDialog dialog = new ContactsDialog();
//                Bundle args = new Bundle();
//                args.putString(ContactsDialog.DIALOG_TYPE, ContactsDialog.DELETE_CONTACT);
//                args.putInt(ContactsContract.ContactsColumns.CONTACTS_ID, id);
//                args.putString(ContactsContract.ContactsColumns.CONTACT_NAME, name);
//                dialog.setArguments(args);
//                dialog.show(sFragmentManager, "delete-contact");
//            }
//        });

            view.setOnTouchListener(new View.OnTouchListener() {

                int initialX = 0;
                final float slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int showedDialog = 0;

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        initialX = (int) event.getX();
                        Log.d(LOG_TAG,"InitialX = " + initialX);
                        view.setPadding(0, 0, 0, 0);
                        showedDialog = 0;
                        Log.d(LOG_TAG, "Padding set to 0,0,0,0 ");

                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        int currentX = (int) event.getX();
                        int offset = currentX - initialX;

                        if (Math.abs(offset) > slop && offset > 0) {
                            view.setPadding(offset, 0, 0, 0);
                            Log.d(LOG_TAG, "Padding set to "+offset+" ,0,0,0 ");

                            if (offset >= DEFAULT_THRESHOLD && showedDialog ==0 ) {
                                Log.d(LOG_TAG, "Offset = " + offset + " > " + DEFAULT_THRESHOLD);
                                showedDialog++;
                                // Left to right - delete action
                                ContactsDialog dialog = new ContactsDialog();
                                Bundle args = new Bundle();
                                args.putString(ContactsDialog.DIALOG_TYPE, ContactsDialog.DELETE_CONTACT);
                                args.putInt(ContactsContract.ContactsColumns.CONTACTS_ID, id);
                                args.putString(ContactsContract.ContactsColumns.CONTACT_NAME, name);
                                dialog.setArguments(args);
                                dialog.show(sFragmentManager, "delete-contact");

                                //animate back
                                ValueAnimator animator = ValueAnimator.ofInt(view.getPaddingLeft(), 0);
                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        view.setPadding((Integer) valueAnimator.getAnimatedValue(), 0, 0, 0);
                                    }
                                });
                                animator.setDuration(150);
                                animator.start();

                            } else if (offset < - DEFAULT_THRESHOLD) {
                                //Do nothing on swipe left
                            }
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        // Animate back if no action was performed.
                        ValueAnimator animator = ValueAnimator.ofInt(view.getPaddingLeft(), 0);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                view.setPadding((Integer) valueAnimator.getAnimatedValue(), 0, 0, 0);
                            }
                        });
                        animator.setDuration(150);
                        animator.start();
                    }

                    return false;
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
