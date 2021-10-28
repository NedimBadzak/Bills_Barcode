package com.nedim.probabarcode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

class SendPOST extends AsyncTask<Void, Void, String> {
    public Context mainActivityContext;
    ProgressDialog pdLoading;

    HashMap<String, String> params;

    public SendPOST(Activity mainActivity, String username, String password) {
        params = new HashMap<>();
        params.put("requestReason", "login");
        params.put("username", username);
        params.put("password", password);
        pdLoading = new ProgressDialog(mainActivity);
        this.mainActivityContext = mainActivity.getApplicationContext();
    }

    public SendPOST(Activity mainActivity, HashMap<String, String> params) {
        this.params = params;
        pdLoading = new ProgressDialog(mainActivity);
        this.mainActivityContext = mainActivity.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        //creating request handler object
        RequestHandler requestHandler = new RequestHandler();
        SharedPreferences sharedPreferences = mainActivityContext.getSharedPreferences(
                "postavke",
                Context.MODE_PRIVATE
        );
        String ip = "";
        if (sharedPreferences.getString("ip", "0.0.0.0").equals("0.0.0.0")) {
            ip = "192.168.0.105";
        } else {
            ip = sharedPreferences.getString("ip", "0.0.0.0");
        }

        //returing the response
        return requestHandler.sendPostRequest("http://" + ip + "/android/index.php", params);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pdLoading.dismiss();

        try {
            Log.d("TAGIC", s);
            //converting response to json object
            JSONObject obj = new JSONObject(s);
            //if no error in response
            if(obj.getInt("success") == 3) {
                Log.v("TAGIC", obj.getString("message"));
                JSONArray jsonArray = new JSONArray(obj.getString("message"));
                Toast.makeText(mainActivityContext, "Reference fethed in clipboard", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) mainActivityContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(jsonArray.getJSONObject(0).getString("sta"), jsonArray.getJSONObject(0).getString("referenca"));
                clipboard.setPrimaryClip(clip);
            } else if(obj.getInt("success") == 1) {
                Toast.makeText(mainActivityContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
            }  else {
                Toast.makeText(mainActivityContext, obj.getString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mainActivityContext, "Exception: " + e, Toast.LENGTH_LONG).show();
            Log.v("LOGIC", e.getMessage().toString());
        }
    }
}
