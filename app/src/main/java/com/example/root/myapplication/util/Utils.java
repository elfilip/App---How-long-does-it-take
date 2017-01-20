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
            int millis=(int)(time.getTime()%1000);
            if (millis < 10) {
                result.append("00");
            }else if(millis<100){
                result.append("0");
            }
            result.append(millis);
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

    public static String limitCharPerLines(String text,int charNum) {
        if(text.length()<charNum)
            return text;

        StringBuilder sb = new StringBuilder();
        String[] words = text.split(" ");

        int count=0;
        for (String word : words) {
            if(word.length()>charNum){
                if(count!=0) {
                    sb.append(" ");
                    count++;
                }
                for(int i=0;i<word.length();i++) {
                    sb.append(word.charAt(i));
                    count++;
                    if(count==charNum){
                        sb.append('\n');
                        count=0;
                    }
                }
            }
            else if(count+word.length()<charNum){
                if(count!=0 && word.length()!=0)  {
                    sb.append(" ");
                    count++;
                }
                if(word.length()==0){
                    sb.append(" ");
                    count++;
                }else{
                    sb.append(word);
                    count += word.length();
                }
            }else{
                sb.append('\n').append(word);
                count=word.length();
            }
        }
        return sb.toString();
    }
}
