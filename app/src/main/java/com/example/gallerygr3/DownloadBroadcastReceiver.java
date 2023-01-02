package com.example.gallerygr3;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class DownloadBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        ImageDisplay ic= ImageDisplay.getInstance();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            String[] result=new String[1];
            ic.dates.add((new Date()).toString());
            File kl= new File(ic.fullNameFile);
            ic.size.add( Integer.parseInt(String.valueOf(kl.length()/1024)));
            result[0]=ic.fullNameFile;
            if(ic.fullNameFile=="") return;
            Toast.makeText((MainActivity) ic.getContext(), "Download down", Toast.LENGTH_SHORT).show();
            ((MainActivity)ic.getContext()).addImageUpdate(result);
            // ((MainActivity)getContext()).DownloadImageFromURLVer3(url_input[0].trim(),url_input[1].trim());
            ic.notifyChangeGridLayout();
        }
    }
}