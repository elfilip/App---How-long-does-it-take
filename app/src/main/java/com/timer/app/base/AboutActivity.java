package com.timer.app.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.timer.app.base.util.Constants;

/**
 * Created by felias on 5.12.16.
 */

public class AboutActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(myToolbar);
        this.setTitle(R.string.about);
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

    public void conditionsClick(View view) {
        Intent intent = new Intent(this, ShowTxtAssetActivity.class);
        intent.putExtra(Constants.ASSET_NAME, "license.txt");
        startActivity(intent);
    }

    public void reportIssueClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/elfilip/App---How-long-does-it-take/issues"));
        startActivity(browserIntent);
    }

    public void logClick(View view) {
        Intent intent = new Intent(this, ShowTxtAssetActivity.class);
        intent.putExtra(Constants.ASSET_NAME, "changeLog.txt");
        startActivity(intent);
    }
}
