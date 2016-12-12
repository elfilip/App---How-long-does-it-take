package com.example.root.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.Status;
import com.example.root.myapplication.fragment.DetailCardsAdapter;
import com.example.root.myapplication.util.Constants;
import com.example.root.myapplication.util.MyApplication;
import com.example.root.myapplication.util.Utils;

/**
 * Created by felias on 21.11.16.
 */

public class DetailActivity extends AppCompatActivity {

    private MyApplication app;
    private boolean isNameEdit=false;
    private TextView measuringCounter;
    private ViewPager cardsPager;
    private DetailCardsAdapter cardsAdapter;
    private Action currentAction;

    public static int CODE=102;
    public static int RESULT_CODE_UPDATE=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=MyApplication.getInstance(getFilesDir());
        setContentView(R.layout.action_details);
        Intent intent = getIntent();
        String actionName = intent.getStringExtra(Constants.ACTION_NAME);
        currentAction=app.getAction(actionName);

        cardsAdapter = new DetailCardsAdapter(getSupportFragmentManager(),currentAction);
        cardsPager = (ViewPager) findViewById(R.id.cards_pager);
        cardsPager.setAdapter(cardsAdapter);

        final TextView name=(TextView)findViewById(R.id.detail_name);
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



            name.setText(currentAction.getName());
            name.setEnabled(false);
            name_editText.setText(currentAction.getName());



        final ImageButton startNextButton = (ImageButton) findViewById(R.id.start_next_button);
        startNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.saveStatus(new  Status(currentAction.getName(), SystemClock.elapsedRealtime(),CODE));
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                intent.putExtra(MainActivity.ACTION_NAME, currentAction.getName());
                intent.putExtra(Constants.REQUEST_CODE, CODE);
                startActivityForResult(intent,CODE);
                setResult(DetailActivity.RESULT_CODE_UPDATE);
            }
        });

        final ImageButton deleteButton = (ImageButton) findViewById(R.id.delete_mes_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentAction.hasMoreMeasurements()==false){
                    Utils.showAlertDialog(DetailActivity.this,R.string.cant_delete,R.string.cant_delete_last,R.string.ok);
                    return;
                }
                if (cardsPager.getCurrentItem() != 0) {
                    cardsPager.setCurrentItem(cardsPager.getCurrentItem()-1);
                }
                currentAction.deleteMeasurement(cardsPager.getCurrentItem());
                setResult(DetailActivity.RESULT_CODE_UPDATE);
                updateAllFields();
            }
        });

        ImageButton previous = (ImageButton) findViewById(R.id.but_previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardsPager.setCurrentItem(cardsPager.getCurrentItem() - 1);
            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.but_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardsPager.setCurrentItem(cardsPager.getCurrentItem() + 1);
            }

        });

        measuringCounter=(TextView)findViewById(R.id.current_measuring);
        setMeasuringCounter();

        cardsPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setMeasuringCounter();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        Toolbar myToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(myToolbar);
        this.setTitle(R.string.action_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void setMeasuringCounter() {
        measuringCounter.setText((cardsPager.getCurrentItem()+1)+" / "+currentAction.getMeasurement().size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
       finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

         updateAllFields();
    }


    private void updateAllFields(){
        cardsAdapter.notifyDataSetChanged();
        cardsPager.setCurrentItem(cardsAdapter.getCount()-1);
        setMeasuringCounter();
    }

}
