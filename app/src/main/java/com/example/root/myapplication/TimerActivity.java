package com.example.root.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.Status;
import com.example.root.myapplication.util.MyApplication;
import com.example.root.myapplication.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by felias on 8.11.16.
 */

public class TimerActivity extends AppCompatActivity {
    private Chronometer timer;
    private boolean paused = false;
    private long timeWhenStopped = 0;
    private String actionNameVar;
    private MyApplication app;
    private boolean isStoped = false;
    Bitmap bitMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.timer);
            Intent intent = getIntent();
            String message = intent.getStringExtra(MainActivity.ACTION_NAME);
            long timerBase=intent.getLongExtra(MainActivity.TIMER_BASE,-1);
            TextView actionName = (TextView) findViewById(R.id.actionName);
            actionName.setText(message);
            actionNameVar = message;
            timer = new Chronometer(this);
            if (timerBase != -1) {
                timer.setBase(timerBase);
            }
            System.out.println("TimerActivityOnCreate");
            app = MyApplication.getInstance(getApplicationContext().getFilesDir());
            final TimerCanvas customCanvas = (TimerCanvas) findViewById(R.id.signature_canvas);
            timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
                @Override
                public void onChronometerTick(Chronometer cArg) {
                    long time = SystemClock.elapsedRealtime() - cArg.getBase();
                    int h   = (int)(time /3600000);
                    int m = (int)(time - h*3600000)/60000;
                    int s= (int)(time - h*3600000- m*60000)/1000 ;
                    String hh = h < 10 ? "0"+h: h+"";
                    String mm = m < 10 ? "0"+m: m+"";
                    String ss = s < 10 ? "0"+s: s+"";
                    cArg.setText(hh+":"+mm+":"+ss);
                    customCanvas.setSeconds(s);
                    customCanvas.setTimeText(hh+":"+mm+":"+ss);
                    customCanvas.invalidate();
                }
            });
            timer.start();


        }catch(Exception e){
            System.out.println("Filip");
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("timerBase", timer.getBase());
        System.out.println("TimerActivitySave");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            System.out.println("TimerActivityRestore");
            timer.setBase(savedInstanceState.getLong("timerBase"));
            super.onRestoreInstanceState(savedInstanceState);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void pauseClick(View view) {

        Button bpause = (Button) findViewById(R.id.bPause);
        if (paused == false) {
            bpause.setText(R.string.button_resume);
            timeWhenStopped = timer.getBase() - SystemClock.elapsedRealtime();
            timer.stop();
        } else {
            bpause.setText(R.string.button_pause);
            timer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            timer.start();
        }
        paused = !paused;
    }

    public void stopClick(View view) {
        timer.stop();
        FileOutputStream outputStream = null;
        Action action = new Action(actionNameVar, getTime(timer), new Date());
        MyApplication app = MyApplication.getInstance(getApplicationContext().getFilesDir());
        app.addAction(action);
        Intent intent = new Intent(this, MainActivity.class);
        app.deleteStatusFile();
        isStoped=true;
        startActivity(intent);
    }

    private String getTime(Chronometer timer) {
        long sec = (SystemClock.elapsedRealtime() - timer.getBase()) / 1000;
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

    @Override
    protected void onResume() {
        System.out.println("TimerActivityOnResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        System.out.println("TimeActivityOnStop");
        if(isStoped==false)
            app.saveStatus(new  Status(actionNameVar,timer.getBase()));
        super.onStop();
    }
    @Override
    protected void onPause(){
        System.out.println("TimeActivityOnPause");
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        System.out.println("TimerActivityOnDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        System.out.println("TimerActivityOnRestart");
        super.onRestart();
    }
}
