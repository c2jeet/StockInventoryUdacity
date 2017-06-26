package com.example.abco_abhijeet.stockinventory.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ABCO-Abhijeet on 6/22/2017.
 */

public class ItemContract {
    //prevent accidental instaniation of this class
    private ItemContract()
    {

    }

    public static final String CONTENT_AUTHORITY = "com.example.abco_abhijeet.stockinventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ITEMS = "items";

    public static final class ItemEntry implements BaseColumns
    {
        //Content URI to access Item data
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        //MIME Type of the Content Url Link for item list
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        //MIME Type of the Content for single item
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        //Table Name
        public static final String TABLE_NAME = "items";

        //1. Name of Unique Item ID, Type = INTEGER
        public static final String _ID = BaseColumns._ID;

        //2. Name of Item, Type = TEXT
        public static final String COLUMN_ITEM_NAME = "name";

        //3. Type of Item, Type = TEXT
        public static final String COLUMN_ITEM_TYPE = "type";

        //4. Model of Item, Type = TEXT
        public static final String COLUMN_ITEM_MODEL = "model";

        //5. Quantity of Item, Type = INTEGER
        public static final String COLUMN_ITEM_QUANTITY = "quantity";

        //6. Price of Item, Type = INTEGER
        public static final String COLUMN_ITEM_PRICE = "price";

        //7. Image of Item, Type = TEXT
        public static final String COLUMN_ITEM_IMAGE = "image";

        //8. Manufacturer Name of Item, Type = TEXT
        public static final String COLUMN_ITEM_MFG_NAME = "mfgName";

        //9. Manufacturer Address of Item, Type = TEXT
        public static final String COLUMN_ITEM_MFG_ADDRESS = "mfgAddress";

        //10. Manufacturer Phone of Item, Type = TEXT
        public static final String COLUMN_ITEM_MFG_PHONE = "mfgPhone";

        //11. Manufacturer Email of Item, Type = TEXT
        public static final String COLUMN_ITEM_MFG_EMAIL = "mfgEmail";
    }
}
