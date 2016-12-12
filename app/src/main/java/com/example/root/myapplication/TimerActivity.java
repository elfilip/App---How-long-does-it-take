package com.example.root.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.Measurement;
import com.example.root.myapplication.entity.Status;
import com.example.root.myapplication.util.Constants;
import com.example.root.myapplication.util.MyApplication;

import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by felias on 8.11.16.
 */

public class TimerActivity extends AppCompatActivity {
    private Chronometer timer;
    private boolean paused = false;
    private long timeWhenStopped = 0;

    private MyApplication app;
    private EditText noteView;
    public static final int RESULT_CODE =2;
    private int request_code;
    private boolean isNoteEdit=false;
    private Status currentStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            app = MyApplication.getInstance(getApplicationContext().getFilesDir());
            setContentView(R.layout.timer);
            Intent intent = getIntent();
            System.out.println("oncreate0");

            noteView = (EditText) findViewById(R.id.timer_note);

            if(app.statusExist()==true){
                currentStatus=app.loadStatus();
            }else{
                System.out.println("No status file");
                throw new RuntimeException("Status file must exist");
            }

            System.out.println("jmeno je " + currentStatus.getActionName());

            String nameUpperCase=currentStatus.getActionName().substring(0,1).toUpperCase().concat(currentStatus.getActionName().substring(1));
            long timerBase=currentStatus.getTimerBase();
            request_code = currentStatus.getRequestCode();
            if(currentStatus.getNote()!=null && currentStatus.getNote().length()!=0){
                noteView.setText(currentStatus.getNote());
            }
            System.out.println("oncreate1");
            TextView actionName = (TextView) findViewById(R.id.actionName);
            actionName.setText(nameUpperCase);
            timer = (Chronometer)findViewById(R.id.chronometer);
            if (timerBase != -1) {
                timer.setBase(timerBase);
            }
            final ImageButton noteButton = (ImageButton) findViewById(R.id.note_ok_button);

            noteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noteView.setFocusableInTouchMode(true);
                    noteView.setFocusable(true);
                    noteView.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(noteView, InputMethodManager.SHOW_IMPLICIT);
                    isNoteEdit=!isNoteEdit;
                    noteButton.setVisibility(View.VISIBLE);
                }
            });


            noteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noteView.setFocusableInTouchMode(false);
                    noteView.setFocusable(false);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(noteView.getWindowToken(), 0);
                    isNoteEdit=!isNoteEdit;
                    noteButton.setVisibility(View.GONE);
                    if(noteView.getText().toString().trim().length()!=0) {
                        currentStatus.setNote(noteView.getText().toString().trim());
                    }
                    app.saveStatus(currentStatus);
                    isNoteEdit=!isNoteEdit;
                    noteButton.setVisibility(View.GONE);
                }
            });
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
            setResult(TimerActivity.RESULT_CODE);


        }catch(Exception e){
            System.out.println("Filip");
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
     //   savedInstanceState.putLong("timerBase", timer.getBase());
     //   System.out.println("TimerActivitySave");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
          //  System.out.println("TimerActivityRestore");
          //  timer.setBase(savedInstanceState.getLong("timerBase"));
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
        String noteText;
        if (noteView != null && noteView.getText().toString().trim().length() != 0) {
            noteText=noteView.getText().toString();
        }else{
            noteText = "";
        }
        Measurement mes = new Measurement(new Date(getTime(timer)), new Date(), noteText);
        Action action=null;
        if(request_code == DetailActivity.CODE) {
            action = app.getAction(currentStatus.getActionName());
        }else{
            action = new Action(currentStatus.getActionName());
            app.addAction(action);
        }
        assert action!=null;
        action.addMeasurement(mes);
        app.deleteStatusFile();
        app.saveActions();
        finish();
    }

    private long getTime(Chronometer timer) {
        return (SystemClock.elapsedRealtime() - timer.getBase());

    }

    @Override
    protected void onResume() {
        System.out.println("TimerActivityOnResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        System.out.println("TimeActivityOnStop "+timer.getBase());
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
