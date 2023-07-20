package com.nedim.probabarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class IPSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_settings);
        SharedPreferences sharedPreferences = this.getSharedPreferences("postavke",
                Context.MODE_PRIVATE);
        EditText editText = findViewById(R.id.ipEditText);
        Button ipButton = findViewById(R.id.ipButton);

        editText.setText(sharedPreferences.getString("ip", "0.0.0.0"));
        ipButton.setOnClickListener(v -> {
//            editText.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ip", editText.getText().toString());
            editor.apply();
            editor.commit();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}