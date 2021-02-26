package com.nedim.probabarcode;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BillDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String ime, skenirano;
    BillDAO(String ime, String skenirano) {
        this.ime = ime;
        this.skenirano = skenirano;
    }

    public void insert() {
        Map<String, Object> bill = new HashMap<>();
        bill.put("ime", ime);
        bill.put("skenirano", skenirano);

        db.collection("bills")
                .add(bill)
                .addOnSuccessListener(documentReference -> Log.d("TAGIC", "Insertovan sa id: " + documentReference.getId()))
                .addOnFailureListener(documentReference -> Log.d("TAGIC", "Failed"));
    }
}
