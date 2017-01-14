package com.example.root.myapplication;

import android.app.DialogFragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.root.myapplication.dialog.AreYouSureDialog;
import com.example.root.myapplication.dialog.DeleteDialog;
import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.Status;
import com.example.root.myapplication.fragment.ActionListAdapter;
import com.example.root.myapplication.fragment.FileDialog;
import com.example.root.myapplication.storage.FileStorage;
import com.example.root.myapplication.service.AppService;
import com.example.root.myapplication.storage.JSONConfigurationStorage;
import com.example.root.myapplication.util.Utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Main application activity. Shows list of all actions.
 *
 */
public class MainActivity extends AppCompatActivity implements DeleteDialog.DeleteAllDialogListener {

    private AppService app;
    private ActionListAdapter adapter; //adapter for displaying list of actions
    public final static int CODE=101; //request code for this activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewGroup layout=(ViewGroup)findViewById(R.id.action_list);

        app = AppService.getInstance();
        if(!app.isStorageSet()){
            app.setStorage(new FileStorage(getApplicationContext().getFilesDir()),
                           new JSONConfigurationStorage(getApplicationContext().getFilesDir()));
        }
    System.out.println(getApplicationContext().getFilesDir().getAbsolutePath());
        //creates list of actions
        createList(layout);
        //checks whether timers is started and opens timer activity if so
        checkIfTimerCounts();
        //filters list of actions based on the search field
        applyFilterSearch();

        //Configure toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        app.saveStatus(new  Status(message, SystemClock.elapsedRealtime(),CODE));
        startActivityForResult(intent,CODE);

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
                startActivityForResult(settingsIntent,CODE);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Called when user clicks OK on delete all popup
     *
     * @param fragment
     */
    public void okClick(DialogFragment fragment){
        app.deleteAllActions();
        adapter.deleteAll();
    }

    /**
     * Called when user click CANCEL on delete all popup
     *
     * @param fragment
     */
    public void cancelClick(DialogFragment fragment){
        fragment.dismiss();
    }

    private void createList(ViewGroup layout){

        final ListView grid=(ListView) findViewById(R.id.action_list);
        adapter = new ActionListAdapter( app.getActions(),this);
        grid.setAdapter(adapter);
    }

    private void checkIfTimerCounts(){
            if(app.statusExist()) {
                Intent intent = new Intent(this, TimerActivity.class);
                startActivityForResult(intent, CODE);
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
        ((NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
        if (requestCode == MainActivity.CODE) {
            // Update list of actions if it changed
            if (resultCode == DetailActivity.RESULT_CODE_UPDATE || resultCode==TimerActivity.RESULT_CODE) {
                adapter.notifyDataSetChanged();
            }
        }
    }

}
