package com.example.root.myapplication;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.root.myapplication.dialog.DeleteDialog;
import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.Status;
import com.example.root.myapplication.fragment.ActionListAdapter;
import com.example.root.myapplication.util.Constants;
import com.example.root.myapplication.util.MyApplication;
import com.example.root.myapplication.util.Utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeleteDialog.DeleteAllDialogListener {
    public final static String ACTION_NAME = "com.example.myfirstapp.MESSAGE";
    public final static String TIMER_BASE = "com.example.myfirstapp.TIMERBASE";
    private MyApplication app;
    private ActionListAdapter adapter ;
    public final static int CODE=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup layout=(ViewGroup)findViewById(R.id.action_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(android.R.drawable.ic_menu_myplaces);
        app = MyApplication.getInstance(getApplicationContext().getFilesDir());
        createList(layout);
        checkIfTimerCounts();
        applyFilterSearch();

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putLong("AAA", 10000);
        System.out.println("MainActivitySave");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        System.out.println("MainActivityRestore");

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /** Called when the user clicks the Send button */
    public void clickStart(View view) {

        Intent intent = new Intent(this, TimerActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        if(message == null || message.length()==0){
            Utils.showAlertDialog(this,R.string.invalid_input,R.string.please_select_name,R.string.ok);
            return;
        }else if(app.checkIfActionExists(message)){
            Utils.showAlertDialog(this,R.string.invalid_input,R.string.name_exits,R.string.ok);
            return;
        }
        app.saveStatus(new  Status(message, SystemClock.elapsedRealtime()));
        intent.putExtra(ACTION_NAME, message);
        intent.putExtra(Constants.REQUEST_CODE, CODE);
        startActivityForResult(intent,CODE);

    }

    public void getTestX(View view){
        Intent intent=new Intent(this,TestActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("Options selected");
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                DialogFragment dialog = new DeleteDialog();
                dialog.show(getFragmentManager(), "NoticeDialogFragment");
                return true;


            case R.id.action_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            case R.id.action_help:
                Intent helpIntent = new Intent(this, HelpActivity.class);
                startActivity(helpIntent);
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void okClick(DialogFragment fragment){
        app.deleteAllActions();
        deleteAllActionViews();
    }

    public void cancelClick(DialogFragment fragment){
        fragment.dismiss();
    }

    private void createList(ViewGroup layout){

        final ListView grid=(ListView) findViewById(R.id.action_list);
        adapter = new ActionListAdapter( app.getActions(),this);
        grid.setAdapter(adapter);
    }

    private void deleteAllActionViews(){
        ListView grid=(ListView) findViewById(R.id.action_list);
        app.deleteAllActions();
        adapter.setFiltered(new LinkedList<Action>());
        adapter.notifyDataSetChanged();
    }

    private void checkIfTimerCounts(){
        File file = new File(getFilesDir(), Constants.STATUS_FILE);
        Status status = app.loadStatus();
        if (status != null) {
            Intent intent = new Intent(this, TimerActivity.class);
            EditText editText = (EditText) findViewById(R.id.edit_message);
            intent.putExtra(ACTION_NAME, status.getActionName());
            intent.putExtra(TIMER_BASE, status.getTimerBase());
            if (status.getRequestCode() != -1) {
                intent.putExtra(Constants.REQUEST_CODE, DetailActivity.CODE);
            }
            startActivityForResult(intent,CODE);
        }
    }

    private void applyFilterSearch(){
        final EditText search=(EditText) findViewById(R.id.search_field);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<Action> filteredActions=new LinkedList<>();
                String filterText = search.getText().toString().trim().toLowerCase();
                if(filterText.length()==0){
                    adapter.setFiltered(app.getActions());
                }else {
                    for (Action a : app.getActions()) {
                        if (a.getName().toLowerCase().contains(filterText)) {
                            filteredActions.add(a);
                        }
                    }
                    adapter.setFiltered(filteredActions);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MainActivity.CODE) {
            // Make sure the request was successful
            if (resultCode == DetailActivity.RESULT_CODE_UPDATE || resultCode==TimerActivity.RESULT_CODE) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
