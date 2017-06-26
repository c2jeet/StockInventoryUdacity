package com.example.abco_abhijeet.stockinventory;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abco_abhijeet.stockinventory.db.ItemContract;
import com.squareup.picasso.Picasso;

/**
 * Created by ABCO-Abhijeet on 6/22/2017.
 */

public class ItemsCursorAdapter extends CursorAdapter
{

    //Initialization Constructor
    public ItemsCursorAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {

        //Find and Link Views to Variables
        TextView nameTextView = (TextView) view.findViewById(R.id.item_name_view);
        TextView quantityTextView = (TextView) view.findViewById(R.id.item_quantity_view);
        ImageView imagePlaceHolderView = (ImageView) view.findViewById(R.id.item_image_view);
        TextView priceTextView = (TextView) view.findViewById(R.id.item_price_view);

        Button sellActionButtonView = (Button) view.findViewById(R.id.item_sale_action_button);
        //Set Sell Button To Color Scheme
        sellActionButtonView.getBackground().setColorFilter(Color.parseColor("#f1c40f"), PorterDuff.Mode.MULTIPLY);

        //Find columns of Items Attributes
        int idColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY);
        int imageColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_IMAGE);

        //Read item attributes for the cursor
        int id = cursor.getInt(idColumnIndex);
        final String itemName = cursor.getString(nameColumnIndex);
        final int itemPrice = cursor.getInt(priceColumnIndex);
        final int itemQuantity = cursor.getInt(quantityColumnIndex);
        Uri imageUri = Uri.parse(cursor.getString(imageColumnIndex));

        //Push data into the declared views
        nameTextView.setText(itemName);
        priceTextView.setText("Rs. " + String.valueOf(itemPrice));
        if (itemQuantity > 0)
        {
            quantityTextView.setText(String.valueOf(itemQuantity) + " Items Left in Stock");
        }
        else
        {
            quantityTextView.setText("Item is out of stock!");
        }

        //Picasso loads the Image Uri into the image view
        Picasso.with(context).load(imageUri).placeholder(R.mipmap.ic_launcher).fit().centerInside().into(imagePlaceHolderView);

        final Uri mCurrentUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, id);

        sellActionButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver resolver = view.getContext().getContentResolver();
                ContentValues values = new ContentValues();
                if (itemQuantity > 0)
                {
                    int qty = itemQuantity;
                    values.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, --qty);
                    resolver.update(
                            mCurrentUri,
                            values,
                            null,
                            null
                    );
                    context.getContentResolver().notifyChange(mCurrentUri, null);
                }
                else
                {
                    Toast.makeText(context, itemName + " is out of stock!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
