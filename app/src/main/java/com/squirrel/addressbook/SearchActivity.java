package com.squirrel.addressbook;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 * Created by squirrel on 12/6/15.
 */
public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Contact>> {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    private ContactsAdapter mContactsAdapter;
    private static int LOADER_ID = 2;
    private ContentResolver mContentResolver;
    private List<Contact> mContacts;
    private ListView mListView;
    private EditText mSearchInput;
    private Button mSearchButton;
    private String matchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        mListView = (ListView) findViewById(R.id.search_results_list);
        mSearchButton = (Button) findViewById(R.id.search_button);
        mSearchInput = (EditText) findViewById(R.id.search_input);
        mContentResolver = getContentResolver();
        mContactsAdapter = new ContactsAdapter(SearchActivity.this, getSupportFragmentManager());
        mListView.setAdapter(mContactsAdapter);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchText = mSearchInput.getText().toString();
                getSupportLoaderManager().initLoader(LOADER_ID++, null, SearchActivity.this);
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {
        mContactsAdapter.setData(data);
        this.mContacts = data;
    }

    @Override
    public void onLoaderReset(Loader<List<Contact>> loader) {
        mContactsAdapter.setData(null);
    }

    @Override
    public Loader<List<Contact>> onCreateLoader(int id, Bundle args) {
        return new ContactsSearchListLoader(SearchActivity.this, ContactsContract.URI_TABLE, this.mContentResolver, matchText);
    }
}
