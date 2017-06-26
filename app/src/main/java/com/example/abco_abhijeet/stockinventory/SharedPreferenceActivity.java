package com.example.abco_abhijeet.stockinventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SharedPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preference);

        int sharedPreferencesQuantity = 1;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String quantity = preferences.getString("Quantity", "");
        if (!quantity.equalsIgnoreCase(""))
        {
            sharedPreferencesQuantity = Integer.parseInt(quantity);
        }

        TextView currentSharedPreference = (TextView) findViewById(R.id.shared_preference_current_value);
        currentSharedPreference.setText(String.valueOf(sharedPreferencesQuantity));

        final EditText sharedPrefernceInputEt = (EditText) findViewById(R.id.shared_preference_qty_et);
        Button sharedPreferenceAction = (Button) findViewById(R.id.shared_preference_save_action_btn);

        sharedPreferenceAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Quantity", sharedPrefernceInputEt.getText().toString().trim());
                editor.apply();

                Toast.makeText(getApplicationContext(), "Preference Added Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
