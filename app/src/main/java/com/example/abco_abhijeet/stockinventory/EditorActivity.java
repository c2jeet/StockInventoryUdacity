package com.example.abco_abhijeet.stockinventory;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abco_abhijeet.stockinventory.db.ItemContract;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditorActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{

    //identifier for item data loaders
    private static final int EXISTING_ITEM_LOADER = 0;
    public static final int PICK_PHOTO_REQUEST = 20;
    public static final int EXTERNAL_STORAGE_REQUEST_PERMISSION_CODE = 21;

    //Content URI for the item
    private Uri mCurrentUri;
    private Uri mProductPhotoUri;

    private String mCurrentPhotoUri = "";
    private int sharedPreferencesQuantity = 1;

    //Define Views of the Field
    //1
    private EditText mItemNameEt;
    //2
    private EditText mItemTypeEt;
    //3
    private EditText mItemModelEt;
    //4
    private EditText mItemQuantityEt;
    //5
    private EditText mItemPriceEt;
    //6
    private ImageView mItemImageBtn;
    //7
    private EditText mItemMfgNameEt;
    //8
    private EditText mItemMfgAddressEt;
    //9
    private EditText mItemMfgPhoneEt;
    //10
    private EditText mItemMfgEmailEt;

    //11
    private ImageView mItemImagePlaceHolder;

    //boolean variable to keep track if the Editor Screen has been changed or not
    private boolean mItemHasChanged = false;

    //OnTouchListeter that keeps track if someone has touched the view
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Fetch Data from Shared Preference for use in Quantity Increment and Decrement Button Actions
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String quantity = preferences.getString("Quantity", "");
        if (!quantity.equalsIgnoreCase(""))
        {
            sharedPreferencesQuantity = Integer.parseInt(quantity);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Map declared Edit Text variables to their view ids and assign setOnTouchListeners to them
        //1
        mItemNameEt = (EditText) findViewById(R.id.item_name_et);
        mItemNameEt.setOnTouchListener(mTouchListener);
        //2
        mItemTypeEt = (EditText) findViewById(R.id.item_type_et);
        mItemTypeEt.setOnTouchListener(mTouchListener);
        //3
        mItemModelEt = (EditText) findViewById(R.id.item_model_et);
        mItemModelEt.setOnTouchListener(mTouchListener);
        //4
        mItemQuantityEt = (EditText) findViewById(R.id.item_quantity_et);
        mItemQuantityEt.setOnTouchListener(mTouchListener);
        //5
        mItemPriceEt = (EditText) findViewById(R.id.item_price_et);
        mItemPriceEt.setOnTouchListener(mTouchListener);
        //6
        mItemImageBtn = (ImageView) findViewById(R.id.item_image_button);
        mItemImageBtn.setOnTouchListener(mTouchListener);
        //7
        mItemMfgNameEt = (EditText) findViewById(R.id.item_mfg_name_et);
        mItemMfgNameEt.setOnTouchListener(mTouchListener);
        //8
        mItemMfgAddressEt = (EditText) findViewById(R.id.item_mfg_address_et);
        mItemMfgAddressEt.setOnTouchListener(mTouchListener);
        //9
        mItemMfgPhoneEt = (EditText) findViewById(R.id.item_mfg_phone_et);
        mItemMfgPhoneEt.setOnTouchListener(mTouchListener);
        //10
        mItemMfgEmailEt = (EditText) findViewById(R.id.item_mfg_email_et);
        mItemMfgEmailEt.setOnTouchListener(mTouchListener);
        //11
        mItemImagePlaceHolder = (ImageView) findViewById(R.id.item_image_button);
        mItemImagePlaceHolder.setOnTouchListener(mTouchListener);
        //12
        Button incrementAction = (Button) findViewById(R.id.item_quantity_increment_action);
        //set button color
        incrementAction.getBackground().setColorFilter(Color.parseColor("#f1c40f"), PorterDuff.Mode.MULTIPLY);
        incrementAction.setOnTouchListener(mTouchListener);
        //13
        Button decrementAction = (Button) findViewById(R.id.item_quantity_decrement_action);
        //set button color
        decrementAction.getBackground().setColorFilter(Color.parseColor("#f1c40f"), PorterDuff.Mode.MULTIPLY);
        decrementAction.setOnTouchListener(mTouchListener);

        //load uri here
        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        if (mCurrentUri == null)
        {
            setTitle("Add New Item");
        }
        else
        {
            setTitle("Edit Existing Item");
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }

        mItemImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageUpdate(v);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //define increment action with shared preferences
        incrementAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValueString = mItemQuantityEt.getText().toString();
                int currentValueInt;
                if (currentValueString.isEmpty())
                {
                    currentValueInt = 0;
                }
                else
                {
                    currentValueInt = Integer.parseInt(currentValueString);
                }
                mItemQuantityEt.setText(String.valueOf(currentValueInt + sharedPreferencesQuantity));
            }
        });

        //define decrement action with shared preferences
        decrementAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValueString = mItemQuantityEt.getText().toString();
                int currentValueInt = Integer.parseInt(currentValueString);
                if (currentValueString.isEmpty())
                {
                    return;
                }
                else if (currentValueInt <= 0)
                {
                    return;
                }
                else if ((currentValueInt - sharedPreferencesQuantity) < 0)
                {
                    mItemQuantityEt.setText(String.valueOf(currentValueInt - 1));
                    return;
                }
                else
                {
                    mItemQuantityEt.setText(String.valueOf(currentValueInt - sharedPreferencesQuantity));
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_REQUEST_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            startGetPhoto();
        }
        else
        {
            Toast.makeText(this, "User has to grant the permissions to access image!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onImageUpdate(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                startGetPhoto();
            }
            else
            {
                String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissionRequest, EXTERNAL_STORAGE_REQUEST_PERMISSION_CODE);
            }
        }
        else
        {
            startGetPhoto();
        }
    }

    private void startGetPhoto()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectoy = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectoy.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, PICK_PHOTO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PHOTO_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                mProductPhotoUri = data.getData();
                mCurrentPhotoUri = mProductPhotoUri.toString();

                Picasso.with(this)
                        .load(mProductPhotoUri)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .into(mItemImagePlaceHolder);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete_current_item:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_order_current_item:
                showOrderDialog();
                return true;
            case R.id.fab:
                addItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentUri == null)
        {
            MenuItem deleteAction = menu.findItem(R.id.action_delete_current_item);
            MenuItem orderAction = menu.findItem(R.id.action_order_current_item);
            deleteAction.setVisible(false);
            orderAction.setVisible(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!mItemHasChanged)
        {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                };
        showUnsavedChangesAlert(discardButtonClickListener);
    }

    private void showUnsavedChangesAlert
            (DialogInterface.OnClickListener discardButtonClickListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Unsaved changes will be deleted, Are you sure?");
        builder.setPositiveButton("Yes, Delete Changes", discardButtonClickListener);
        builder.setNegativeButton("No, Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //helper method to add item
    private boolean addItem()
    {
        //Store from Edit Texts into variables
        //1
        String nameString = mItemNameEt.getText().toString().trim();
        //2
        String typeString = mItemTypeEt.getText().toString().trim();
        //3
        String modelString = mItemModelEt.getText().toString().trim();
        //4
        String quantityString = mItemQuantityEt.getText().toString().trim();
        //5
        String priceString = mItemPriceEt.getText().toString().trim();
        //7
        String mfgNameString = mItemMfgNameEt.getText().toString().trim();
        //8
        String mfgAddressString = mItemMfgAddressEt.getText().toString().trim();
        //9
        String mfgPhoneString = mItemMfgPhoneEt.getText().toString().trim();
        //10
        String mfgEmailString = mItemMfgEmailEt.getText().toString().trim();

        //Validate User Input

        boolean isOk = true;

        //Check Null Status

        if (!checkNullStatus(mItemNameEt, "Name"))
        {
            isOk = false;
        }
        if (!checkNullStatus(mItemTypeEt, "Type"))
        {
            isOk = false;
        }
        if (!checkNullStatus(mItemModelEt, "Model"))
        {
            isOk = false;
        }
        if (!checkNullStatus(mItemQuantityEt, "Quantity"))
        {
            isOk = false;
        }
        if (!checkNullStatus(mItemPriceEt, "Price"))
        {
            isOk = false;
        }
        if (!checkNullStatus(mItemMfgNameEt, "Mfg Name"))
        {
            isOk = false;
        }
        if (!checkNullStatus(mItemMfgAddressEt, "Mfg Address"))
        {
            isOk = false;
        }
        if (!checkNullStatus(mItemMfgPhoneEt, "Mfg Phone"))
        {
            isOk = false;
        }
        if (!checkNullStatus(mItemMfgEmailEt, "Mfg Email"))
        {
            isOk = false;
        }
        if (mProductPhotoUri == null && mCurrentUri == null)
        {
            isOk = false;
            Toast.makeText(this, "Item Image Missing", Toast.LENGTH_LONG).show();
        }

        //Further Check Mail and Phone with android inbuilt validator tools

        if (!isValidMail(mfgEmailString))
        {
            isOk = false;
            mItemMfgEmailEt.setError("Email Is Invalid");
        }

        if (!isValidMobile(mfgPhoneString))
        {
            isOk = false;
            mItemMfgPhoneEt.setError("Phone Number is Invalid");
        }

        if (!isOk)
        {
            return false;
        }

        //continue if everything is ok
        if (isOk)
        {
            //Create Convent Values object
            ContentValues values = new ContentValues();
            //1
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, nameString);
            //2
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_TYPE, typeString);
            //3
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_MODEL, modelString);
            //4
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, String.valueOf(quantityString));
            //5
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, String.valueOf(priceString));
            //6
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_IMAGE, mCurrentPhotoUri);
            //7
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_NAME, mfgNameString);
            //8
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_ADDRESS, mfgAddressString);
            //9
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_PHONE, mfgPhoneString);
            //10
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_MFG_EMAIL, mfgEmailString);


            //Check if new Item is added or existing item is updated
            if (mCurrentUri == null)
            {
                Uri newUri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values);
                //Show toast to inform if addition of item was success or failure
                if (newUri == null)
                {
                    Toast.makeText(this, "Error while adding the item", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Item Added Successfully with Uri " + newUri, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
            else
            {
                int rowAffected = getContentResolver().update(mCurrentUri, values, null, null);

                if (rowAffected == 0)
                {
                    Toast.makeText(this, "Error while updating the item", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Item Updated Successfully with Uri " + mCurrentUri, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }

        return true;
    }

    private boolean checkNullStatus(EditText text, String error)
    {
        if (TextUtils.isEmpty(text.getText()))
        {
            text.setError(error + " Is Missing");
            return false;
        }
        else
        {
            text.setError(null);
            return true;
        }
    }

    private boolean isValidMail(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone)
    {
        return Patterns.PHONE.matcher(phone).matches();
    }


    private void showDeleteConfirmationDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete " + mItemNameEt.getText().toString().trim() + "?");
        builder.setPositiveButton("No, Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null)
                {
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Yes, Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem()
    {
        if (mCurrentUri != null)
        {
            int rowDeleted = getContentResolver().delete(mCurrentUri, null, null);

            if (rowDeleted == 0)
            {
                Toast.makeText(this, "Error while deleting this item", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Item deleted successfully with Uri " + mCurrentUri, Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Define our projection
        String[] projection =
                {
                        ItemContract.ItemEntry._ID,
                        ItemContract.ItemEntry.COLUMN_ITEM_NAME,
                        ItemContract.ItemEntry.COLUMN_ITEM_TYPE,
                        ItemContract.ItemEntry.COLUMN_ITEM_MODEL,
                        ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY,
                        ItemContract.ItemEntry.COLUMN_ITEM_PRICE,
                        ItemContract.ItemEntry.COLUMN_ITEM_IMAGE,
                        ItemContract.ItemEntry.COLUMN_ITEM_MFG_NAME,
                        ItemContract.ItemEntry.COLUMN_ITEM_MFG_ADDRESS,
                        ItemContract.ItemEntry.COLUMN_ITEM_MFG_PHONE,
                        ItemContract.ItemEntry.COLUMN_ITEM_MFG_EMAIL
                };

        return new CursorLoader(
                this,
                mCurrentUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1 )
        {
            return;
        }

        if (data.moveToFirst())
        {
            //find the columns of the data
            //1
            int nameColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME);
            //2
            int typeColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_TYPE);
            //3
            int modelColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_MODEL);
            //4
            int quantityColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY);
            //5
            int priceColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE);
            //6
            int imageColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_IMAGE);
            //7
            int mfgNameColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_MFG_NAME);
            //8
            int mfgAddressColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_MFG_ADDRESS);
            //9
            int mfgPhoneColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_MFG_PHONE);
            //10
            int mfgEmailColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_MFG_EMAIL);

            //Extract data out of the value
            String name = data.getString(nameColumnIndex);
            String type = data.getString(typeColumnIndex);
            String model = data.getString(modelColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            int price = data.getInt(priceColumnIndex);
            mCurrentPhotoUri = data.getString(imageColumnIndex);
            String mfgName = data.getString(mfgNameColumnIndex);
            String mfgAddress = data.getString(mfgAddressColumnIndex);
            String mfgPhone = data.getString(mfgPhoneColumnIndex);
            String mfgEmail = data.getString(mfgEmailColumnIndex);

            //Set Data to the Edit Text Views
            mItemNameEt.setText(name);
            mItemTypeEt.setText(type);
            mItemModelEt.setText(model);
            mItemQuantityEt.setText(String.valueOf(quantity));
            mItemPriceEt.setText(String.valueOf(price));
            //use picasso to load image uri into image view
            Picasso.with(this).load(mCurrentPhotoUri).placeholder(R.drawable.uploadimage).into(mItemImagePlaceHolder);
            mItemMfgNameEt.setText(mfgName);
            mItemMfgAddressEt.setText(mfgAddress);
            mItemMfgPhoneEt.setText(mfgPhone);
            mItemMfgEmailEt.setText(mfgEmail);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //defined for use with Quantity and Price Texts
        String zero = "0";

        //Set Data to the Edit Text Views
        mItemNameEt.setText("");
        mItemTypeEt.setText("");
        mItemModelEt.setText("");
        mItemQuantityEt.setText(String.valueOf(zero));
        mItemPriceEt.setText(String.valueOf(zero));
        mItemMfgNameEt.setText("");
        mItemMfgAddressEt.setText("");
        mItemMfgPhoneEt.setText("");
        mItemMfgEmailEt.setText("");
    }

    private void showOrderDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("How would you like to order more " + mItemNameEt.getText().toString().trim() + "?");
        builder.setPositiveButton("Phone", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mItemMfgPhoneEt.getText().toString().trim()));
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" + mItemMfgEmailEt.getText().toString().trim()));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Require New Stock");
                String content = "Require New Stock of " + mItemNameEt.getText().toString().trim() + "ASAP!";
                intent.putExtra(Intent.EXTRA_TEXT, content);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
