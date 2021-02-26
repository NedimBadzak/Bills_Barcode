package com.nedim.probabarcode;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button buttonVoda;
    private Button buttonStruja;
    private Button buttonRad;
    private Button buttonSarajevoStan;
    private Button buttonTelemach;
    private Button buttonToplane;
    private Button buttonHetig;
    private String klikaniButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonVoda = (Button) this.findViewById(R.id.btnVoda);
        buttonStruja = (Button) this.findViewById(R.id.btnStruja);
        buttonRad = (Button) this.findViewById(R.id.btnRad);
        buttonSarajevoStan = (Button) this.findViewById(R.id.btnSarajevoStan);
        buttonTelemach = (Button) this.findViewById(R.id.btnTelemach);
        buttonToplane = (Button) this.findViewById(R.id.btnToplane);
        buttonHetig = (Button) this.findViewById(R.id.btnHetig);
        final Activity activity = this;
        clicked(buttonVoda, activity);
        clicked(buttonStruja, activity);
        clicked(buttonRad, activity);
        clicked(buttonSarajevoStan, activity);
        clicked(buttonTelemach, activity);
        clicked(buttonToplane, activity);
        clicked(buttonHetig, activity);
    }

    private void clicked(Button button, Activity activity) {
        button.setOnClickListener(v -> {
            String ime = getResources().getResourceName(v.getId());
            IntentIntegrator integrator = new IntentIntegrator(activity);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            klikaniButton = button.getText().toString();
            integrator.initiateScan();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                BillDAO billDAO = new BillDAO(dajZamjenu(klikaniButton), result.getContents());
                billDAO.insert();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String dajZamjenu(String shorty) {
        String[] first = new String[]{"epiznos", "vikiznos", "ssiznos", "toplaneiznos", "teleiznos", "radiznos", "hetig"};
        String[] second = new String[]{"Elektroprivreda", "Vodovod i Kanalizacija", "Sarajevo Stan", "Toplane", "Telemach", "Rad", "Hetig"};
        return first[Arrays.asList(second).indexOf(shorty)];
    }
}
