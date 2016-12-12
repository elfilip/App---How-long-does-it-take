package com.example.root.myapplication.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.Date;

/**
 * Created by felias on 14.11.16.
 */

public class Utils {

    public static void showAlertDialog(Context context, int title, int message, int buttonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String convertTimeToText(Date time) {
        long sec = time.getTime() / 1000;
        long seconds = sec % 60;
        long minutes = (sec / 60) % 60;
        long hours = (sec / 60) / 60;
        StringBuilder result = new StringBuilder();
        if (hours < 10) {
            result.append("0");
        }
        result.append(hours);
        result.append(":");
        if (minutes < 10) {
            result.append("0");
        }
        result.append(minutes);
        result.append(":");
        if (seconds < 10) {
            result.append("0");
        }
        result.append(seconds);
        return result.toString();
    }
}
