package com.timer.app.base.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timer.app.base.DetailActivity;
import com.timer.app.base.MainActivity;
import com.timer.app.base.R;
import com.timer.app.base.entity.Action;
import com.timer.app.base.util.Constants;
import com.timer.app.base.service.AppService;
import com.timer.app.base.util.Utils;

import java.util.List;

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
    private final int buttonSize;
    private AppService app;
    private final int MAX_CHARS_PER_LINE=15;

    public ActionListAdapter(List<Action> filtered, AppCompatActivity context) {
        app=AppService.getInstance();
        rows = filtered.size();
        this.context = context;
        final float scale = context.getResources().getDisplayMetrics().density;
        rowHeight = (int) (70 * scale + 0.5f);
        this.filtered = filtered;
        iconSize = getDimension(R.dimen.iconSize);
        buttonSize = getDimension(R.dimen.buttonSize);
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
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nameTimeLayout.setLayoutParams(lp);
        nameTimeLayout.setGravity(Gravity.CENTER_VERTICAL);
        nameTimeLayout.setMinimumHeight(rowHeight);
        //name column

        final LinearLayout rootLayout = new LinearLayout(context);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        rootLayout.setGravity(Gravity.CENTER);
        ImageView iw = new ImageView(context);
        Action currentAction = filtered.get(row);
        if(currentAction.hasMoreMeasurements()==false) {
            iw.setImageResource(R.drawable.cifernik);
        }else{
            iw.setImageResource(R.drawable.cifernik_trio);
        }
        iw.setPadding(getDimension(R.dimen.paddingIconLeft), 0,getDimension( R.dimen.paddingIconRight), 0);
        iw.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams iwparams=new LinearLayout.LayoutParams(iconSize,iconSize);
        iw.setLayoutParams(iwparams);
        iw.setScaleType(ImageView.ScaleType.FIT_CENTER);
        rootLayout.addView(iw);
        rootLayout.setBackgroundColor(Color.WHITE);



            TextView name = new TextView(context);
            LinearLayout.LayoutParams nameParams=new LinearLayout.LayoutParams(getDimension(R.dimen.nameMaxWidth),ViewGroup.LayoutParams.WRAP_CONTENT);
            name.setLayoutParams(nameParams);
            name.setText(Utils.limitCharPerLines(currentAction.getName(),MAX_CHARS_PER_LINE));
            name.setTextSize(19f);
            name.setTextColor(Color.BLACK);
            name.setSingleLine(false);
           // name.setHorizontallyScrolling(true);
            //name.setMovementMethod(new ScrollingMovementMethod());
            name.setPadding(0, 0, 0, 0);
            nameTimeLayout.addView(name);

            TextView time = new TextView(context);
        if(currentAction.hasMoreMeasurements()==false) {
            time.setText(currentAction.getMeasurement().get(0).getTimeText(app.getConfig().isShowMilis()));
        }
        else{

            String dateText = Utils.convertTimeToText(currentAction.getAverageTime(),app.getConfig().isShowMilis()) + " ("+context.getResources().getString(R.string.average)+")";
            time.setText(dateText);
        }
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
            delete.setPadding(0, 0, getDimension(R.dimen.paddingIconRight), 0);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rootLayout.setBackgroundColor(context.getResources().getColor(R.color.selected_delete));
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    app.deleteAction(filtered.get(row).getName());
                                    if (filtered != app.getActions())
                                        filtered.remove(row);
                                    notifyDataSetChanged();
                                    rootLayout.setBackgroundResource(R.drawable.filip_selector);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    rootLayout.setBackgroundResource(R.drawable.filip_selector);
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                            .setNegativeButton(R.string.no, dialogClickListener).show();

                }
            });
            LinearLayout.LayoutParams deleteparams = new LinearLayout.LayoutParams(buttonSize, buttonSize);
            delete.setLayoutParams(deleteparams);
            delete.setScaleType(ImageView.ScaleType.FIT_CENTER);


            final ImageButton detail = new ImageButton(context);
            Drawable iconDetail = context.getResources().getDrawable(R.drawable.info);
            detail.setImageDrawable(iconDetail);
            detail.setBackgroundColor(Color.TRANSPARENT);
            detail.setPadding(0, 0, getDimension(R.dimen.paddingIconRight), 0);
            detail.setClickable(false);
            LinearLayout.LayoutParams detailparams = new LinearLayout.LayoutParams(buttonSize, buttonSize);
            detail.setLayoutParams(detailparams);
            detail.setScaleType(ImageView.ScaleType.FIT_CENTER);
            buttonLayout.addView(detail);
            buttonLayout.addView(delete);
            rootLayout.addView(buttonLayout);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailActivity(filtered.get(row).getName());
            }
        });
        rootLayout.setClickable(true);
        rootLayout.setBackgroundResource(R.drawable.filip_selector);
        return rootLayout;
    }

    private void showDetailActivity(String name) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Constants.ACTION_NAME, name);
        context.startActivityForResult(intent,  MainActivity.CODE);
    }

    public void deleteAll() {
        filtered.clear();
        notifyDataSetChanged();
    }

    int getDimension(int id) {
        return (int) context.getResources().getDimension(id);
    }



}
