package com.nedim.probabarcode;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Button buttonVoda;
    private Button buttonStruja;
    private Button buttonRad;
    private Button buttonSarajevoStan;
    private Button buttonTelemach;
    private Button buttonToplane;
    private Button buttonHetig;
    private String mText = "";
    private TextView apiStatus;
    private Racun racun;
    private String klikaniButton = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SendGET sendGET = new SendGET(this, "/api/checkStatus", apiStatus);
        buttonVoda = (Button) this.findViewById(R.id.btnVoda);
        buttonStruja = (Button) this.findViewById(R.id.btnStruja);
        buttonRad = (Button) this.findViewById(R.id.btnRad);
        buttonSarajevoStan = (Button) this.findViewById(R.id.btnSarajevoStan);
        buttonTelemach = (Button) this.findViewById(R.id.btnTelemach);
        buttonToplane = (Button) this.findViewById(R.id.btnToplane);
        buttonHetig = (Button) this.findViewById(R.id.btnHetig);
        apiStatus = (TextView) this.findViewById(R.id.textView2);
        final Activity activity = this;
        clicked(buttonVoda, activity);
        clicked(buttonStruja, activity);
        clicked(buttonRad, activity);
        clicked(buttonSarajevoStan, activity);
        clicked(buttonTelemach, activity);
        clicked(buttonToplane, activity);
        clicked(buttonHetig, activity);


            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> updateApiStatus());
                }
            };
            timer.schedule(doAsynchronousTask, 0, 10000);



    }

    private boolean updateApiStatus() {
        SendGET sendGET = new SendGET(this, "/api/checkStatus", apiStatus);
        sendGET.execute();
        return false;
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
            integrator.setTorchEnabled(true);
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
                Log.d("MainActivity TAGIC resultContents", "Scanned: " + result.getContents());
                try {
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    Racun racun = napraviRacun(dajZamjenu(klikaniButton), result.getContents());
                } catch (Exception e) {
                    Toast.makeText(this, "Nevazeci scan, molimo ponovite", Toast.LENGTH_SHORT).show();
                }
//                billDAO.insert();
                if(racun.getIznos() != 0) insert(racun);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(racun.getIme(), racun.getReferenca());
                clipboard.setPrimaryClip(clip);
                Log.d("MainActivity TAGIC poruka", "Racun: " + "(" + racun.getIme() + ") sa referencom: "
                        + racun.getReferenca() + " i iznosom: " + racun.getIznos());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ip_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.ipSettings:
                startActivity(new Intent(this, IPSettingsActivity.class));
                return true;
            case R.id.locationSettings:
                startActivity(new Intent(this, LocationSettingsActivity.class));
                return true;
            case R.id.fromImageSettings:
                startActivity(new Intent(this, FromImage.class));
            case R.id.getRefFromDB:
                startActivity(new Intent(this, GetReferenceFromDatabase.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insert(Racun racun) {
        Log.d("TAGIC", "iznos je: " + racun.getIznos());
        JSONObject jsonObject = new JSONObject();
        Date date = Calendar.getInstance().getTime();
        try {
            jsonObject.put("sta", racun.getIme());
            jsonObject.put("iznos", racun.getIznos());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                "postavke",
                Context.MODE_PRIVATE
        );
        String location;
        if (sharedPreferences.getString("location", "pofe").equals("pofe")) {
            location = "pofe";
        } else {
            location = sharedPreferences.getString("location", "pofe");
        }
        HashMap<String, String> params = new HashMap<>();
//        params.put("requestReason", "insertPaidBill");
        params.put("sta", racun.getIme());
        params.put("referenca", racun.getReferenca());
        params.put("iznos", String.valueOf(racun.getIznos()));
        params.put("placeno", String.valueOf(1));
        params.put("godina", new SimpleDateFormat("YYYY").format(date));
        params.put("lokacija", location);
        params.put("mjesec", new SimpleDateFormat("MM").format(date));
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
            Log.v("TAGIC", "Referenca" + referenca);
            Log.v("TAGIC", "prvi substring" + skenirano.indexOf(';') + 1);
            Log.v("TAGIC", "drugi substring" + skenirano.indexOf(';', skenirano.indexOf(';') + 1));
            String km = skenirano.substring(skenirano.indexOf(';') + 1, skenirano.indexOf(','));
            String kf = skenirano.substring(skenirano.indexOf(',') + 1);
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
            builder.setPositiveButton("OK", (dialog, which) -> {
                racun.setIznos(Double.parseDouble(input.getText().toString()));
                insert(racun);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        } else if ("teleiznos".equals(ime)) {
            String bezZnakova = skenirano.replaceAll("\\D+", "");
            String bezZnakova2 = bezZnakova.substring(0,15);
            String[] bezZnakova3 = bezZnakova.split("000");
            if(bezZnakova2.equals(bezZnakova3[0]))
                racun = new Racun("teleiznos", bezZnakova3[0], Double.parseDouble(bezZnakova3[1].substring(0,2) + "." + bezZnakova3[1].substring(2)));
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
