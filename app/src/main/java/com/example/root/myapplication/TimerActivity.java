package com.example.root.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.example.root.myapplication.service.AppService;

import java.io.FileOutputStream;
import java.util.Date;

/**
 * Timer. Shows watch face with time elapsed.
 *
 */
public class TimerActivity extends AppCompatActivity {

    private static String tag=TimerActivity.class.getSimpleName();

    private Chronometer timer;
    private boolean paused = false;
    private long timeWhenStopped = 0;

    private AppService app;
    private EditText noteView;
    private TimerCanvas customCanvas;
    private ImageButton noteButton;
    public static final int RESULT_CODE =2;
    private int request_code;
    private boolean isNoteEdit=false;
    private Status currentStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            app = AppService.getInstance();
            setContentView(R.layout.timer);

            //get references to all components
            noteView = (EditText) findViewById(R.id.timer_note);
            timer = (Chronometer)findViewById(R.id.chronometer);
            TextView actionName = (TextView) findViewById(R.id.actionName);
            noteButton = (ImageButton) findViewById(R.id.note_ok_button);
            customCanvas = (TimerCanvas) findViewById(R.id.signature_canvas);

            if(app.statusExist()==true){
                currentStatus=app.loadStatus();
            }else{
                Log.e(tag,"Status file must exist",new Exception("Status file must exist"));
                finish();
                return;
            }

            //Load current status of the timer, application might be restarted
            String nameUpperCase=currentStatus.getActionName().substring(0,1).toUpperCase().concat(currentStatus.getActionName().substring(1));
            long timerBase=currentStatus.getTimerBase();
            request_code = currentStatus.getRequestCode();
            if(currentStatus.getNote()!=null && currentStatus.getNote().length()!=0){
                noteView.setText(currentStatus.getNote());
            }

            actionName.setText(nameUpperCase);
            //If application was unexpectedly exited, continue with measuring
            if (timerBase != -1) {
                timer.setBase(timerBase);
                //TODO if the phone was restarted just interrupt the timer
                if(timerBase>=SystemClock.elapsedRealtime()){
                    app.deleteStatus();
                    finish();
                }
            }

            //click on note, note will activate itself and confirm button is displayed
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
            displayNotification(nameUpperCase);
            startTimer();
            setResult(TimerActivity.RESULT_CODE);  //list of action in MainActivity will be updated
    }

    @Override
    public void onBackPressed() {
        //intentionally blank, back button is deactivated
    }

    /**
     * Called when button PAUSE is clicked
     *
     * @param view
     */
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
            timeWhenStopped=0;
        }
        paused = !paused;
    }

    /**
     * Called when button STOP is clicked
     *
     * @param view
     */
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
        app.deleteStatus();
        app.saveActions();
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancelAll();
        finish();
    }

    /**
     * Called when note ok button is clicked
     *
     * @param view
     */
    public void noteOKClick(View view) {
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

    private long getTime(Chronometer timer) {
        if(timeWhenStopped==0){
            return (SystemClock.elapsedRealtime() - timer.getBase());
        }else{
            return -timeWhenStopped;
        }
    }

    /**
     * Sets a lister for each tick for canvas and starts the timer
     *
     */
    private void startTimer() {
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
    }

    private void displayNotification(String activityName){
        if(app.getConfig().isShowIcon()) {
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.cancelAll();
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentIntent(pIntent)
                    .setContentText(getApplicationContext().getString(R.string.in_progress,activityName));
            int mNotificationId = 269;
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }
}
