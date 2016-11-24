package com.example.root.myapplication;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.util.Constants;
import com.example.root.myapplication.util.MyApplication;

/**
 * Created by felias on 21.11.16.
 */

public class DetailActivity extends AppCompatActivity {

    private MyApplication app;
    private boolean isNameEdit=false;
    private boolean isNoteEdit=false;

    public static int CODE=101;
    public static int RESULT_CODE_UPDATE=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=MyApplication.getInstance(getFilesDir());
        setContentView(R.layout.action_details);
        Intent intent = getIntent();
        String actionName = intent.getStringExtra(Constants.ACTION_NAME);
        final Action currentAction=app.getAction(actionName);
        final TextView name=(TextView)findViewById(R.id.detail_name);
        final TextView time = (TextView) findViewById(R.id.detail_time);
        final TextView date = (TextView) findViewById(R.id.detail_date);
        final EditText note=(EditText)findViewById(R.id.detail_note);
        final EditText name_editText = (EditText)findViewById(R.id.details_name_edittext);
        final ImageButton editNameButton = (ImageButton) findViewById(R.id.detail_edit_button);
        editNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameEdit==false) {
                    name.setVisibility(View.GONE);
                    name_editText.setVisibility(View.VISIBLE);
                    editNameButton.setImageResource(R.drawable.check_small);
                }else{
                    name.setVisibility(View.VISIBLE);
                    name_editText.setVisibility(View.GONE);
                    editNameButton.setImageResource(R.drawable.edit_image);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                    app.updateActionName(currentAction.getName(),name_editText.getText().toString());
                    name.setText(name_editText.getText().toString());
                    setResult(DetailActivity.RESULT_CODE_UPDATE);
                }
                isNameEdit=!isNameEdit;
            }
        });

        final ImageButton editNoteButton = (ImageButton) findViewById(R.id.detail_note_button);
        editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNoteEdit==false) {
                    editNoteButton.setImageResource(R.drawable.check_small);
                    note.setFocusableInTouchMode(true);
                    note.setFocusable(true);
                    note.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(note, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    note.setFocusableInTouchMode(false);
                    note.setFocusable(false);
                    editNoteButton.setImageResource(R.drawable.edit_image);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(note.getWindowToken(), 0);
                    app.updateActionNote(currentAction.getName(),note.getText().toString());
                    setResult(DetailActivity.RESULT_CODE_UPDATE);
                }
                isNoteEdit=!isNoteEdit;
            }
        });

        name.setText(currentAction.getName());
        name.setEnabled(false);
        name_editText.setText(currentAction.getName());
        time.setText(currentAction.getTime());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateText = sdf.format(currentAction.getDate());
        date.setText(dateText);
        note.setText(currentAction.getNote());


    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
       finish();
    }
}
