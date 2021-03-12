package com.nedim.probabarcode;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button buttonVoda;
    private Button buttonStruja;
    private Button buttonRad;
    private Button buttonSarajevoStan;
    private Button buttonTelemach;
    private Button buttonToplane;
    private Button buttonHetig;
    private String mText = "";
    private Racun racun;
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
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Racun racun = napraviRacun(dajZamjenu(klikaniButton), result.getContents());
//                billDAO.insert();
                insert(racun);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(racun.getIme(), racun.getReferenca());
                clipboard.setPrimaryClip(clip);
                Log.d("TAGIC", "Racun: " + "(" + racun.getIme() + ") sa referencom: "
                        + racun.getReferenca() + " i iznosom: " + racun.getIznos());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void insert(Racun racun) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sta", racun.getIme());
            jsonObject.put("iznos", racun.getIznos());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("requestReason", "insertPaidBill");
        params.put("sta", racun.getIme());
        params.put("iznos", String.valueOf(racun.getIznos()));
        params.put("placeno", String.valueOf(1));
        params.put("godina", String.valueOf(2021));
        params.put("lokacija", "pofe");
        params.put("mjesec", String.valueOf(8));
        SendPOST sendPOST = new SendPOST(this, params);
        sendPOST.execute();
    }

    private Racun napraviRacun(String ime, String skenirano) {
        if ("ssiznos".equals(ime)) {
            String bezNula = skenirano.replace("000", "-");
            int indexPocetka = bezNula.indexOf("325034050");
            String prviDio = bezNula.substring(0, indexPocetka);
            String referenca = "325034050" + prviDio;
            racun = new Racun("ssiznos", referenca, 15.20);
        } else if ("toplaneiznos".equals(ime)) {
            String referenca = skenirano.substring(0, skenirano.indexOf(';'));
            String km = skenirano.substring(skenirano.indexOf(';') + 1, skenirano.indexOf(';', skenirano.indexOf(';') + 1));
            String kf = skenirano.substring(skenirano.indexOf(';', skenirano.indexOf(';') + 1) + 1);
            String pare = km + "." + kf;
            racun = new Racun("toplaneiznos", referenca, Double.parseDouble(pare));
        } else if ("radiznos".equals(ime)) {
            String referenca = skenirano.substring(0, skenirano.indexOf('-'));
            racun = new Racun("radiznos", referenca, 5.35);
        } else if ("vikiznos".equals(ime) || "epiznos".equals(ime)) {
            racun = new Racun(ime, skenirano);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Title");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    racun.setIznos(Double.parseDouble(input.getText().toString()));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }
        return racun;
    }

    private void setMText(String s) {
        mText = s;
    }


    private String dajZamjenu(String shorty) {
        String[] first = new String[]{"epiznos", "vikiznos", "ssiznos", "toplaneiznos", "teleiznos", "radiznos", "hetig"};
        String[] second = new String[]{"Elektroprivreda", "Vodovod i Kanalizacija", "Sarajevo Stan", "Toplane", "Telemach", "Rad", "Hetig"};
        return first[Arrays.asList(second).indexOf(shorty)];
    }
}
