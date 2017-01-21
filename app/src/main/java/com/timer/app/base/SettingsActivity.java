package com.timer.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.timer.app.base.entity.Configuration;
import com.timer.app.base.service.AppService;

/**
 * Created by felias on 5.12.16.
 */

public class SettingsActivity extends AppCompatActivity {

    private AppService app;
    private Configuration configuration;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=AppService.getInstance();
        configuration=app.getConfig();
        setContentView(R.layout.activity_settings);

        CheckBox timerWhite = (CheckBox) findViewById(R.id.time_white);
        CheckBox showIcon = (CheckBox) findViewById(R.id.show_icon);
        CheckBox showMillis = (CheckBox) findViewById(R.id.show_millis);

        timerWhite.setChecked(configuration.isTimeWhiteColor());
        showIcon.setChecked(configuration.isShowIcon());
        showMillis.setChecked(configuration.isShowMilis());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);
        this.setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

    public void onTimerWhiteClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        configuration.setTimeWhiteColor(checked);
        setResult(DetailActivity.RESULT_CODE_UPDATE);
        app.saveConfig(configuration);
    }

    public void onShowIconClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        configuration.setShowIcon(checked);
        setResult(DetailActivity.RESULT_CODE_UPDATE);
        app.saveConfig(configuration);
    }

    public void onShowMillisClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        configuration.setShowMilis(checked);
        setResult(DetailActivity.RESULT_CODE_UPDATE);
        app.saveConfig(configuration);
    }


}
