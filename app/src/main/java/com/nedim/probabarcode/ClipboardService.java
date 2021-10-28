package com.nedim.probabarcode;
/*
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ClipboardService extends Service {
    String label;
    String text;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        this.label = intent.getStringExtra("label");
        this.text = intent.getStringExtra("text");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
//        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData clip = ClipData.newPlainText(label, text);
//        clipboard.setPrimaryClip(clip);
        return super.onStartCommand(intent, flags, startId);
    }
}
*/