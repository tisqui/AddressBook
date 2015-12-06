package com.squirrel.addressbook;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;

/**
 * Created by squirrel on 12/5/15.
 */
public class ContactsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Contact>> {

    private static final String LOG_TAG = ContactsListFragment.class.getSimpleName();
    private ContactsAdapter mContactsAdapter;
    private static final int LOADER_ID = 1;
    private ContentResolver mContentResolver;
    private List<Contact> mContacts;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mContentResolver = getActivity().getContentResolver();
        mContactsAdapter = new ContactsAdapter(getActivity(), getChildFragmentManager());
        //set the default text on the screen when there is no data
        setEmptyText("No contacts");
        setListAdapter(mContactsAdapter);
        setListShown(false); //wait until we get data before showing it
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Contact>> onCreateLoader(int id, Bundle args) {
        mContentResolver = getActivity().getContentResolver();
        return new ContactsListLoader(getActivity(), ContactsContract.URI_TABLE, mContentResolver);
    }

    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {
        mContactsAdapter.setData(data);
        mContacts = data;
        if(isResumed()){
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Contact>> loader) {
        mContactsAdapter.setData(null);//delete the data that is in data set
    }
}
