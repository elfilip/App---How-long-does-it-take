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
import android.widget.ImageView;
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

public class ActionListAdapter extends BaseAdapter {

    private int rows;
    private List<Action> filtered;
    private AppCompatActivity context;
    private final String color1 = "#ffffff";
    private final String color2 = "#ffffff";
    private int rowHeight;
    private final int iconSize;
    private final int butonSize;

    public ActionListAdapter(List<Action> filtered, AppCompatActivity context) {

        rows = filtered.size();
        this.context = context;
        final float scale = context.getResources().getDisplayMetrics().density;
        rowHeight = (int) (70 * scale + 0.5f);
        this.filtered = filtered;
        iconSize = (int) (50 * scale + 0.5f);
        butonSize = (int) (40 * scale + 0.5f);
    }

    public void setFiltered(List<Action> newlist) {
        filtered = newlist;
    }

    @Override
    public int getCount() {
        return filtered.size();
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

        float textSize = 19f;
        final int row = pos;
        LinearLayout nameTimeLayout = new LinearLayout(context);
        nameTimeLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, rowHeight);
        nameTimeLayout.setLayoutParams(lp);
        nameTimeLayout.setGravity(Gravity.CENTER_VERTICAL);

        //name column

        LinearLayout rootLayout = new LinearLayout(context);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        rootLayout.setGravity(Gravity.CENTER);
        ImageView iw = new ImageView(context);
        iw.setImageResource(R.drawable.cifernik);
        iw.setPadding(10, 0, 40, 0);
        iw.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams iwparams=new LinearLayout.LayoutParams(iconSize,iconSize);
        iw.setLayoutParams(iwparams);
        iw.setScaleType(ImageView.ScaleType.FIT_CENTER);
        rootLayout.addView(iw);
        rootLayout.setBackgroundColor(Color.WHITE);

        TextView name = new TextView(context);
        name.setText(filtered.get(row).getName());
        name.setTextSize(19f);
        name.setTextColor(Color.BLACK);
        name.setSingleLine(true);
        name.setHorizontallyScrolling(true);
        name.setMovementMethod(new ScrollingMovementMethod());
        name.setPadding(0, 0, 0, 0);

       // configureView(name, row, viewGroup);
        nameTimeLayout.addView(name);

        TextView time = new TextView(context);
        time.setText(filtered.get(row).getTime());
        time.setTextSize(15f);
        time.setTextColor(Color.GRAY);
        time.setPadding(0, 0, 0, 0);
       // configureView(time, row, viewGroup);
        nameTimeLayout.addView(time);
        rootLayout.addView(nameTimeLayout);


        LinearLayout buttonLayout = new LinearLayout(context);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams buttonLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        buttonLayout.setLayoutParams(buttonLayoutParams);
        buttonLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        final ImageButton delete = new ImageButton(context);
        Drawable icon = context.getResources().getDrawable(R.drawable.delete);
        delete.setImageDrawable(icon);
        delete.setBackgroundColor(Color.TRANSPARENT);
        delete.setPadding(0, 0, 40, 0);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication app = MyApplication.getInstance(context.getFilesDir());
                app.deleteAction(filtered.get(row).getName());
                if (filtered != app.getActions())
                    filtered.remove(row);
                notifyDataSetChanged();
            }
        });
        LinearLayout.LayoutParams deleteparams=new LinearLayout.LayoutParams(butonSize,butonSize);
        delete.setLayoutParams(deleteparams);
        delete.setScaleType(ImageView.ScaleType.FIT_CENTER);



        final ImageButton detail = new ImageButton(context);
        Drawable iconDetail = context.getResources().getDrawable(R.drawable.info);

        detail.setImageDrawable(iconDetail);
        detail.setBackgroundColor(Color.TRANSPARENT);
       // detail.setTint(android.R.color.holo_blue_dark);
        detail.setPadding(0,0,40,0);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailActivity(filtered.get(row).getName());
            }
        });
        LinearLayout.LayoutParams detailparams=new LinearLayout.LayoutParams(butonSize,butonSize);
        detail.setLayoutParams(detailparams);
        detail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        buttonLayout.addView(detail);
        buttonLayout.addView(delete);
        rootLayout.addView(buttonLayout);

        return rootLayout;
    }

    private void showDetailActivity(String name) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Constants.ACTION_NAME, name);
        context.startActivityForResult(intent, DetailActivity.CODE);
        // context.startActivity(intent);
    }

}
