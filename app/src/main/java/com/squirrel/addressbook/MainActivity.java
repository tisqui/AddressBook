package com.squirrel.addressbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentById(android.R.id.content) == null){
            //if the root view is null
            ContactsListFragment contactsListFragment = new ContactsListFragment();
            fragmentManager.beginTransaction().add(android.R.id.content, contactsListFragment).commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.add_item:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_item:
                ContactsDialog dialog = new ContactsDialog();
                Bundle bundle = new Bundle();
                bundle.putString(ContactsDialog.DIALOG_TYPE, ContactsDialog.DELETE_DB);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(),"delete-db");
                break;
            case R.id.search_item:
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                break;
        }



        return super.onOptionsItemSelected(item);
    }
}
