package com.example.root.myapplication.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

    public static String convertTimeToText(Date time, boolean listMiliseconds) {
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
        if (listMiliseconds == true) {
            result.append(".");
            result.append(time.getTime()%1000);
        }
        return result.toString();
    }

    public static String readFile(File file) {
        FileInputStream fis = null;
        String str = "";

        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't load file");
        }

        if (fis != null) {
            return readInputStream(fis);
        }else{
            return null;
        }
    }

    public static String readInputStream(InputStream fis) {
        String str = "";
        try {
            int content;
            while ((content = fis.read()) != -1) {
                // convert to char and display it
                str += (char) content;
            }

            System.out.println("After reading file");
            System.out.println(str);
            return str;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
