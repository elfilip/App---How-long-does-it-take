package com.example.root.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.root.myapplication.service.AppService;
import com.example.root.myapplication.util.Utils;

/**
 * Created by felias on 21.11.16.
 */

public class DetailActivity extends AppCompatActivity {


    public final static int CODE = 102;
    public final static int RESULT_CODE_UPDATE = 1;

    private AppService app;
    private boolean isNameEdit = false;
    private TextView measuringCounter;
    private ViewPager cardsPager;
    private DetailCardsAdapter cardsAdapter;
    private Action currentAction;
    private TextView name;
    private EditText name_editText;
    private ImageButton editNameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = AppService.getInstance();
        setContentView(R.layout.action_details);
        Intent intent = getIntent();
        String actionName = intent.getStringExtra(Constants.ACTION_NAME);

        name = (TextView) findViewById(R.id.detail_name);
        editNameButton = (ImageButton) findViewById(R.id.detail_edit_name_button);
        name_editText = (EditText) findViewById(R.id.details_name_edittext);
        cardsPager = (ViewPager) findViewById(R.id.cards_pager);
        ImageButton previous = (ImageButton) findViewById(R.id.but_previous);
        ImageButton next = (ImageButton) findViewById(R.id.but_next);
        measuringCounter = (TextView) findViewById(R.id.current_measuring);

        currentAction = app.getAction(actionName); //get action for which we want to load details
        cardsAdapter = new DetailCardsAdapter(getSupportFragmentManager(), currentAction);
        cardsPager.setAdapter(cardsAdapter);

        name.setText(currentAction.getName());
        name.setEnabled(false);
        name_editText.setText(currentAction.getName());

        //when user clicks on show previous measurement
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardsPager.setCurrentItem(cardsPager.getCurrentItem() - 1);
            }
        });

        //when user clicks on show next measurement
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardsPager.setCurrentItem(cardsPager.getCurrentItem() + 1);
            }

        });

        setMeasuringCounter();

        cardsPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                setMeasuringCounter();
            }

            @Override
            public void onPageScrollStateChanged(int state) {            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(myToolbar);
        this.setTitle(R.string.action_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    /**
     * Called when user clicks edit name button
     *
     * @param view
     */
    public void editNameButtonClick(View view) {
        if (isNameEdit == false) {
            name.setVisibility(View.GONE);
            name_editText.setVisibility(View.VISIBLE);
            editNameButton.setImageResource(R.drawable.check_small);
        } else {
            name.setVisibility(View.VISIBLE);
            name_editText.setVisibility(View.GONE);
            editNameButton.setImageResource(R.drawable.edit_image);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
            app.updateActionName(currentAction.getName(), name_editText.getText().toString());
            name.setText(name_editText.getText().toString());
            setResult(DetailActivity.RESULT_CODE_UPDATE);
        }
        isNameEdit = !isNameEdit;
    }

    /**
     * Called when user clicks on delete measurement
     *
     * @param view
     */
    public void deleteButtonClick(View view) {
        if (currentAction.hasMoreMeasurements() == false) {
            Utils.showAlertDialog(DetailActivity.this, R.string.cant_delete, R.string.cant_delete_last, R.string.ok);
            return;
        }

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        if (cardsPager.getCurrentItem() != 0) {
                            cardsPager.setCurrentItem(cardsPager.getCurrentItem() - 1);
                        }
                        currentAction.deleteMeasurement(cardsPager.getCurrentItem());
                        setResult(DetailActivity.RESULT_CODE_UPDATE);
                        updateAllFields();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();

    }

    /**
     * Called when user clicks start next measurement
     *
     * @param view
     */
    public void startNextButtonClick(View view) {
        app.saveStatus(new Status(currentAction.getName(), SystemClock.elapsedRealtime(), CODE));
        Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
        startActivityForResult(intent, CODE);
        setResult(DetailActivity.RESULT_CODE_UPDATE);
    }

    private void setMeasuringCounter() {
        measuringCounter.setText((cardsPager.getCurrentItem() + 1) + " / " + currentAction.getMeasurement().size());
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


    private void updateAllFields() {
        cardsAdapter.notifyDataSetChanged();
        cardsPager.setCurrentItem(cardsAdapter.getCount() - 1);
        setMeasuringCounter();
    }

}
