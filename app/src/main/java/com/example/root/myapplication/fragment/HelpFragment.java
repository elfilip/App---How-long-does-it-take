package com.example.root.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.myapplication.R;

/**
 * Created by felias on 7.12.16.
 */

public class HelpFragment extends Fragment {

    public static String IMAGE_ID = "image_id";
    public static String TEXT_ID = "text_id";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LinearLayout rootView = new LinearLayout(container.getContext());
        rootView.setOrientation(LinearLayout.VERTICAL);
        ImageView image = new ImageView(container.getContext());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.8f);
        image.setLayoutParams(imageParams);
        Bundle args=getArguments();
        image.setImageResource(args.getInt(IMAGE_ID));

        TextView helpText=new TextView(container.getContext());
        helpText.setText(args.getInt(TEXT_ID,R.string.help_default));
        helpText.setTextSize(20f);
        helpText.setPadding(40,5,40,5);
        LinearLayout.LayoutParams helpTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.2f);
        helpText.setLayoutParams(helpTextParams);

        rootView.addView(image);
        rootView.addView(helpText);

        return rootView;
    }

}
