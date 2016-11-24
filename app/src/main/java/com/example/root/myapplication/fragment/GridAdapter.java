package com.example.root.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.myapplication.DetailActivity;
import com.example.root.myapplication.R;
import com.example.root.myapplication.TimerActivity;
import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.util.Constants;
import com.example.root.myapplication.util.MyApplication;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by felias on 10.11.16.
 */

public class GridAdapter extends BaseAdapter {

    int rows;
    int cols;
   // private List<Action> actions;
    private List<Action> filtered;
    private AppCompatActivity context;
    private final String color1 = "#c9cacc";
    private final String color2 = "#e3e3e5";
    private int rowHeight;

    public GridAdapter(List<Action> filtered, int cols, AppCompatActivity context) {

        rows = filtered.size();
        this.cols = cols;
        this.context = context;
        final float scale = context.getResources().getDisplayMetrics().density;
        rowHeight = (int) (40 * scale + 0.5f);
        this.filtered=filtered;
    }

    public void setFiltered(List<Action> newlist) {
        filtered=newlist;
    }

    @Override
    public int getCount() {
        return filtered.size() * cols;
    }

    @Override
    public Object getItem(int pos) {
        return filtered.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {

        float textSize = 21f;

        final int row = pos / cols;
        final int col = pos % cols;
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rowHeight);
        linearLayout.setLayoutParams(lp);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        if (row % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor(color1));
        } else {
            linearLayout.setBackgroundColor(Color.parseColor(color2));
        }
        switch (col) {
            case 0: //name column
                TextView name = new TextView(context);
                name.setText(filtered.get(row).getName());
                name.setTextSize(textSize);
                name.setSingleLine(true);
                name.setHorizontallyScrolling(true);
                name.setMovementMethod(new ScrollingMovementMethod());
                name.setPadding(5, 0, 0, 0);
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDetailActivity(filtered.get(row).getName());
                    }
                });
                configureView(name, row, viewGroup);
                linearLayout.addView(name);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDetailActivity(filtered.get(row).getName());
                }
            });
                break;
            case 1://time column
                TextView time = new TextView(context);
                time.setText(filtered.get(row).getTime());
                time.setTextSize(textSize);
                time.setPadding(5, 0, 0, 0);
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDetailActivity(filtered.get(row).getName());
                    }
                });
                configureView(time, row, viewGroup);
                linearLayout.addView(time);
                linearLayout.setGravity(Gravity.CENTER);

                //delete column
                LinearLayout deleteLayout = new LinearLayout(context);
                deleteLayout.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams dlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                deleteLayout.setLayoutParams(dlp);
                deleteLayout.setGravity(Gravity.RIGHT);
                final ImageButton delete = new ImageButton(context);
                Drawable icon = context.getResources().getDrawable(android.R.drawable.ic_delete);
                delete.setImageDrawable(icon);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyApplication app = MyApplication.getInstance(context.getFilesDir());
                        app.deleteAction(filtered.get(row).getName());
                        if(filtered!=app.getActions())
                        filtered.remove(row);
                        notifyDataSetChanged();
                    }
                });
                deleteLayout.addView(delete);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.addView(deleteLayout);

                break;
            default:
                throw new RuntimeException("Internal Error: Invalid column value");

        }

        return linearLayout;
    }

    private void configureView(View view, int row, ViewGroup parent) {
        if (row % 2 == 0) {
            view.setBackgroundColor(Color.parseColor(color1));
        } else {
            view.setBackgroundColor(Color.parseColor(color2));
        }
        view.setBackgroundColor(Color.TRANSPARENT);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    private void showDetailActivity(String name) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Constants.ACTION_NAME, name);
        context.startActivityForResult(intent,DetailActivity.CODE);
       // context.startActivity(intent);
    }

}
