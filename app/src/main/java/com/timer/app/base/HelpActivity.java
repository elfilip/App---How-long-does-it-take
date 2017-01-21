package com.timer.app.base;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.timer.app.base.fragment.HelpPagerAdapter;

/**
 * Created by felias on 7.12.16.
 */

public class HelpActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int partialProgress = 100 / HelpPagerAdapter.COUNT;
    private ViewPager mViewPager;

    public HelpActivity() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.help_toolbar);
        setSupportActionBar(myToolbar);
        this.setTitle(R.string.help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(partialProgress);
        HelpPagerAdapter adapter = new HelpPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.help_pager);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                progressBar.setProgress((position + 1) * partialProgress);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ImageButton previous = (ImageButton) findViewById(R.id.but_previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.but_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }

        });


    }

    @Override
    public void onBackPressed() {

        if(mViewPager.getCurrentItem()==0){
            finish();
        }
        else{
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
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

}