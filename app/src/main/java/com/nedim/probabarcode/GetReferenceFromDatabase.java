package com.nedim.probabarcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class GetReferenceFromDatabase extends AppCompatActivity {
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
        setContentView(R.layout.activity_get_reference_from_database);
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
            klikaniButton = button.getText().toString();
            Calendar date = Calendar.getInstance();
            HashMap<String, String> params = new HashMap<>();
            params.put("requestReason", "getSingleBill");
            params.put("sta", dajZamjenu(klikaniButton));
            params.put("godina", new SimpleDateFormat("YYYY").format(date.getTime()).toString());
            date.add(Calendar.MONTH, -1);
            params.put("mjesec", new SimpleDateFormat("MM").format(date.getTime()).toString());
            SendPOST sendPOST = new SendPOST(this, params);
            sendPOST.execute();
        });
    }

    private String dajZamjenu(String shorty) {
        String[] first = new String[]{"epiznos", "vikiznos", "ssiznos", "toplaneiznos", "teleiznos", "radiznos", "hetig"};
        String[] second = new String[]{"Elektroprivreda", "Vodovod i Kanalizacija", "Sarajevo Stan", "Toplane", "Telemach", "Rad", "Hetig"};
        return first[Arrays.asList(second).indexOf(shorty)];
    }
}