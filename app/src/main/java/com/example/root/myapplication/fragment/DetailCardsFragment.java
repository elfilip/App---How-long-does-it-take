package com.example.root.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.myapplication.DetailActivity;
import com.example.root.myapplication.MainActivity;
import com.example.root.myapplication.R;
import com.example.root.myapplication.TimerActivity;
import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.Measurement;
import com.example.root.myapplication.entity.Status;
import com.example.root.myapplication.util.Constants;
import com.example.root.myapplication.util.MyApplication;

/**
 * Created by felias on 7.12.16.
 */

public class DetailCardsFragment extends Fragment {

    public static String IMAGE_ID = "image_id";
    public static String TEXT_ID = "text_id";
    public boolean isNoteEdit=false;
    private MyApplication app;
    private Measurement measurement;
    private Action action;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        app = MyApplication.getInstance(null);

        Bundle args=getArguments();
        action=app.getAction(args.getString(DetailCardsAdapter.ACTION_NAME));
        measurement=action.getMeasurement().get(args.getInt(DetailCardsAdapter.MEASUREMENT_PARAM,0));

        View rootView = inflater.inflate(R.layout.fragment_cards, null);
       // container.addView(rootView);

        final TextView time = (TextView) rootView.findViewById(R.id.detail_time);
        final TextView date = (TextView) rootView.findViewById(R.id.detail_date);
        final EditText note=(EditText)rootView.findViewById(R.id.detail_note);

        final ImageButton editNoteButton = (ImageButton)rootView.findViewById(R.id.detail_note_button);
        editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNoteEdit==false) {
                    editNoteButton.setImageResource(R.drawable.check_small);
                    note.setFocusableInTouchMode(true);
                    note.setFocusable(true);
                    note.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(note, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    note.setFocusableInTouchMode(false);
                    note.setFocusable(false);
                    editNoteButton.setImageResource(R.drawable.edit_image);
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(note.getWindowToken(), 0);

                        app.updateActionNote(action.getName(), 0, note.getText().toString());

                }
                isNoteEdit=!isNoteEdit;
            }
        });


            time.setText(measurement.getTimeText());
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String dateText = sdf.format(measurement.getDate());
            date.setText(dateText);
            note.setText(measurement.getNote());

        return rootView;


    }



}
