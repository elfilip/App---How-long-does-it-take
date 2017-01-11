package com.example.root.myapplication;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.root.myapplication.util.Constants;
import com.example.root.myapplication.util.Utils;

import java.io.IOException;

/**
 * Created by elfilip on 8.1.17.
 */

public class ShowTxtAssetActivity extends AppCompatActivity {

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.show_txtasset_activity);

            String fileName=getIntent().getExtras().getString(Constants.ASSET_NAME);

            AssetManager am = getApplicationContext().getAssets();
            TextView licenseText = (TextView) findViewById(R.id.licenseText);
            try {
                licenseText.setText(Utils.readInputStream(am.open(fileName)));
            } catch (IOException e) {
                throw new RuntimeException("Can't load "+fileName+" file)");
            }
        }
}
