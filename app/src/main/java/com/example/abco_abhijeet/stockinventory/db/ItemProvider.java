package com.example.abco_abhijeet.stockinventory.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ABCO-Abhijeet on 6/22/2017.
 */

public class ItemProvider extends ContentProvider{

    //Tag for log messages
    public static final String LOG_TAG = ItemContract.ItemEntry.class.getSimpleName();

    //Uri matcher code for the content URI for entire items table
    private static final int ITEMS = 100;

    //Uri matcher code for the content URI for a single item in items table
    private static final int ITEM_ID = 101;

    //Uri matcher object
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer when the class is called for the first time
    static
    {
        //for the entire table
        mUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS, ITEMS);

        //for individual data of the table
        mUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS + "/#", ITEM_ID);
    }

    //Database helper object
    private ItemDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ItemDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        //Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //Declare Cursor
        Cursor cursor;

        //Check if URI Matcher makes a match with any specific code?
        int match = mUriMatcher.match(uri);
        switch (match)
        {
            case ITEMS:
                cursor = database.query(ItemContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEM_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]
                        {
                                String.valueOf(ContentUris.parseId(uri))
                        };

                cursor = database.query(ItemContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Current Unknow URI can not be queried upon, URI is : " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        //return cursor
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = mUriMatcher.match(uri);
        switch (match)
        {
            case ITEMS:
                return insertItem(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for this current URI : " + uri);
        }
    }

    public Uri insertItem(Uri uri, ContentValues values)
    {
        String name = values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_NAME);
        if (name == null)
        {
            throw new IllegalArgumentException("Item is empty!");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);

        if (id == -1 )
        {
            Log.e(LOG_TAG, "Failed to insert new row for the uri: " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = mUriMatcher.match(uri);
        switch (match)
        {
            case ITEMS:
                return updateItem(uri, values, selection, selectionArgs);
            case ITEM_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]
                        {
                                String.valueOf(ContentUris.parseId(uri))
                        };
                return updateItem(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Failed to update for the uri: " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        if (values.containsKey(ItemContract.ItemEntry.COLUMN_ITEM_NAME))
        {
            String name = values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_NAME);
            if (name == null)
            {
                throw new IllegalArgumentException("Item is empty!");
            }
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowUpdated = database.update(ItemContract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowUpdated != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowDeleted;

        final int match = mUriMatcher.match(uri);
        switch (match)
        {
            case ITEMS:
                rowDeleted = database.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]
                        {
                                String.valueOf(ContentUris.parseId(uri))
                        };
                rowDeleted = database.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Failed to delete for the uri : " + uri);
        }

        if (rowDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match)
        {
            case ITEMS:
                return ItemContract.ItemEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return ItemContract.ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Error! Unknown URI " + uri + " encountered with match " + match);
        }
    }
}
