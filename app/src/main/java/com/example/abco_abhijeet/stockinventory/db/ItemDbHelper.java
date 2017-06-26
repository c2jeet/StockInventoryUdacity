package com.example.abco_abhijeet.stockinventory.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ABCO-Abhijeet on 6/22/2017.
 */

public class ItemDbHelper extends SQLiteOpenHelper {

    //Name of the DB File
    private static final String DATABASE_NAME = "items.db";

    //Version of the DB File
    private static final int DATABASE_VERSION = 1;

    //Constructor defined here
    public ItemDbHelper (Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String Containing SQL Statement to Create the Table

        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME + " ("
                + ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemContract.ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_TYPE + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_MODEL + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_IMAGE + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_MFG_NAME + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_MFG_ADDRESS + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_MFG_PHONE + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_MFG_EMAIL + " TEXT NOT NULL);";

        //Execute the Create String
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
