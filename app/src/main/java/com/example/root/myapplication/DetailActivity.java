package com.example.root.myapplication;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.util.Constants;
import com.example.root.myapplication.util.MyApplication;

/**
 * Created by felias on 21.11.16.
 */

public class DetailActivity extends AppCompatActivity {

    public MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=MyApplication.getInstance(getFilesDir());
        setContentView(R.layout.action_details);
        Intent intent = getIntent();
        String actionName = intent.getStringExtra(Constants.ACTION_NAME);
        Action a=app.getAction(actionName);
       final EditText name=(EditText)findViewById(R.id.detail_name);
        TextView time = (TextView) findViewById(R.id.detail_time);
        TextView date = (TextView) findViewById(R.id.detail_date);
        EditText note=(EditText)findViewById(R.id.detail_note);
        ImageButton edit = (ImageButton) findViewById(R.id.detail_edit_button);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setEnabled(true);
                name.requestFocus();
            }
        });
        name.setText(a.getName());
        name.setEnabled(false);
        time.setText(a.getTime());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateText = sdf.format(a.getDate());
        date.setText(dateText);
        note.setText(a.getNote());


    }
}
