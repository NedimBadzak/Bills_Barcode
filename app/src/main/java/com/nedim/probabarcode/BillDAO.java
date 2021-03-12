package com.nedim.probabarcode;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BillDAO {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Racun racun;

    BillDAO(Racun racun) {
        this.racun = racun;
    }


    public void insert() {
        Map<String, Object> bill = new HashMap<>();
        bill.put("ime", racun.getIme());
        bill.put("skenirano", racun.getSkenirano());

        db.collection("bills")
                .add(bill)
                .addOnSuccessListener(documentReference -> Log.d("TAGIC", "Insertovan sa id: " + documentReference.getId()))
                .addOnFailureListener(documentReference -> Log.d("TAGIC", "Failed"));
    }

}
