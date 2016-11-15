package com.example.root.myapplication;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;

import com.example.root.myapplication.dialog.DeleteDialog;
import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.ActionView;
import com.example.root.myapplication.entity.Status;
import com.example.root.myapplication.fragment.GridAdapter;
import com.example.root.myapplication.util.Constants;
import com.example.root.myapplication.util.MyApplication;
import com.example.root.myapplication.util.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DeleteDialog.DeleteAllDialogListener {
    public final static String ACTION_NAME = "com.example.myfirstapp.MESSAGE";
    public final static String TIMER_BASE = "com.example.myfirstapp.TIMERBASE";
    private Map<String,ActionView> actionViews=new HashMap<>();
    private MyApplication app;
    GridAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            System.out.println("MainActivitySavedBundle");
        }else{
            System.out.println("MainActivityNull");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup layout=(ViewGroup)findViewById(R.id.action_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        app = MyApplication.getInstance(getApplicationContext().getFilesDir());
        System.out.println("MainActivityOnCreate");
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
        intent.putExtra(ACTION_NAME, message);
        startActivity(intent);

    }

    public void getTestX(View view){
        Intent intent=new Intent(this,TestActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                DialogFragment dialog = new DeleteDialog();
                dialog.show(getFragmentManager(), "NoticeDialogFragment");
                return true;

            case R.id.action_reserved:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
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

        final GridView grid=(GridView) findViewById(R.id.action_list);
        adapter = new GridAdapter( app.getActions(),2,this);
        grid.setAdapter(adapter);
    }

    private void deleteAllActionViews(){
        GridView grid=(GridView) findViewById(R.id.action_list);
        grid.removeAllViews();
    }

    private void checkIfTimerCounts(){
        File file = new File(getFilesDir(), Constants.STATUS_FILE);
        Status status = app.loadStatus();
        if (status != null) {
            Intent intent = new Intent(this, TimerActivity.class);
            EditText editText = (EditText) findViewById(R.id.edit_message);
            intent.putExtra(ACTION_NAME, status.getActionName());
            intent.putExtra(TIMER_BASE, status.getTimerBase());
            startActivity(intent);
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
}
