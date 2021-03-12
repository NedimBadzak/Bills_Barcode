package com.nedim.probabarcode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

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


        //returing the response
        return requestHandler.sendPostRequest("http://192.168.0.122/android/index.php", params);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pdLoading.dismiss();

        try {
            //converting response to json object
            JSONObject obj = new JSONObject(s);
            //if no error in response
            if(obj.getInt("success") == 1) {
                Toast.makeText(mainActivityContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
            }  else {
                Toast.makeText(mainActivityContext, obj.getString("message"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mainActivityContext, "Exception: " + e, Toast.LENGTH_LONG).show();
        }
    }
}
