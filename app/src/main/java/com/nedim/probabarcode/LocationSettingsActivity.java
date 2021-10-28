package com.nedim.probabarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LocationSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_settings);
        SharedPreferences sharedPreferences = this.getSharedPreferences("postavke",
                Context.MODE_PRIVATE);
        EditText editText = findViewById(R.id.locationEditText);
        Button submitButton = findViewById(R.id.locationButton);

        submitButton.setOnClickListener(v -> {
//            editText.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("location", editText.getText().toString());
            editor.apply();
            editor.commit();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}