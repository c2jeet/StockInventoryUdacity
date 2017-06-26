package com.example.abco_abhijeet.stockinventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abco_abhijeet.stockinventory.db.ItemContract;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    // Identifier for Items Data Loader
    private static final int ITEM_LOADER = 0;

    // Adapter for List View
    ItemsCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //Find Root ListView in MainActivity to be populated with Items Data from Database
        ListView itemListView = (ListView) findViewById(R.id.root_list_view);


        //Find and set Empty View on the list to be displayed when there is no data to be fetched
        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

        //Setup Adapter to create list item and populate the data on the adapter
        mAdapter = new ItemsCursorAdapter(this, null);
        itemListView.setAdapter(mAdapter);

        //Set up list item click action
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                Uri currentItemUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, id);

                intent.setData(currentItemUri);
                startActivity(intent);
            }
        });

        //Start Loader
        getLoaderManager().initLoader(ITEM_LOADER, null, this);
    }

    //Helper method to insert Dummy Data into our inventory
    private void insertDummyItem()
    {
        ContentValues values1 = new ContentValues();
        values1.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, "Oreo Cookies");
        values1.put(ItemContract.ItemEntry.COLUMN_ITEM_TYPE, "Food & Beverages");
        values1.put(ItemContract.ItemEntry.COLUMN_ITEM_MODEL, "Regular Pack");
        values1.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, 20);
        values1.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, 25);
        values1.put(ItemContract.ItemEntry.COLUMN_ITEM_IMAGE, "android.resource://com.example.abco_abhijeet.stockinventory/drawable/oreo");
        values1.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_NAME, "Cadbury Inc");
        values1.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_ADDRESS, "Delhi - 6");
        values1.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_PHONE, "9861444555");
        values1.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_EMAIL, "support@cadbury.in");

        getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values1);

        ContentValues values2 = new ContentValues();
        values2.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, "Ferrero Rochers");
        values2.put(ItemContract.ItemEntry.COLUMN_ITEM_TYPE, "Food & Beverages");
        values2.put(ItemContract.ItemEntry.COLUMN_ITEM_MODEL, "Regular Pack");
        values2.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, 30);
        values2.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, 300);
        values2.put(ItemContract.ItemEntry.COLUMN_ITEM_IMAGE, "android.resource://com.example.abco_abhijeet.stockinventory/drawable/ferrero_rocher");
        values2.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_NAME, "Ferrero Rochers Inc");
        values2.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_ADDRESS, "Chennai - 6");
        values2.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_PHONE, "9090947256");
        values2.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_EMAIL, "support@frochers.in");

        getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values2);

        ContentValues values3 = new ContentValues();
        values3.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, "Hersheys Spread");
        values3.put(ItemContract.ItemEntry.COLUMN_ITEM_TYPE, "Food & Beverages");
        values3.put(ItemContract.ItemEntry.COLUMN_ITEM_MODEL, "500 Gm");
        values3.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, 40);
        values3.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, 499);
        values3.put(ItemContract.ItemEntry.COLUMN_ITEM_IMAGE, "android.resource://com.example.abco_abhijeet.stockinventory/drawable/hersheys");
        values3.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_NAME, "Hersheys Inc");
        values3.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_ADDRESS, "Kolkata - 6");
        values3.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_PHONE, "7377331433");
        values3.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_EMAIL, "support@hersheys.in");

        getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values3);

        ContentValues values4 = new ContentValues();
        values4.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, "Snickers Chocolate");
        values4.put(ItemContract.ItemEntry.COLUMN_ITEM_TYPE, "Food & Beverages");
        values4.put(ItemContract.ItemEntry.COLUMN_ITEM_MODEL, "Regular Bar");
        values4.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, 10);
        values4.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, 49);
        values4.put(ItemContract.ItemEntry.COLUMN_ITEM_IMAGE, "android.resource://com.example.abco_abhijeet.stockinventory/drawable/snickers");
        values4.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_NAME, "Mars Inc");
        values4.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_ADDRESS, "Mumbai - 6");
        values4.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_PHONE, "7377331433");
        values4.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_EMAIL, "support@mars.in");

        getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values4);
    }

    private void showDeleteConfirmationDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete all your items??");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                {
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllItems();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Helper method to delte all items from the inventory
    private void deleteAllItems()
    {
        int rowDeleted = getContentResolver().delete(ItemContract.ItemEntry.CONTENT_URI, null, null);
        Log.v("MainActivity, ", "Successfully deleted : " + rowDeleted + " from the table");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_dummy_data:
                insertDummyItem();
                Toast.makeText(this, "4 Dummy Items Inserted!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete_allItems:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_redirect_sharedPreference:
                Intent intent = new Intent(MainActivity.this, SharedPreferenceActivity.class);
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        //Define our Projection for ListView
        String[] projection =
                {
                        ItemContract.ItemEntry._ID,
                        ItemContract.ItemEntry.COLUMN_ITEM_NAME,
                        ItemContract.ItemEntry.COLUMN_ITEM_PRICE,
                        ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY,
                        ItemContract.ItemEntry.COLUMN_ITEM_IMAGE
                };

        return new CursorLoader(this,
                ItemContract.ItemEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data)
    {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader)
    {
        mAdapter.swapCursor(null);
    }
}
