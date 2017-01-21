package com.timer.app.base;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.timer.app.base.util.Constants;
import com.timer.app.base.util.Utils;

import java.io.IOException;

/**
 * Created by elfilip on 8.1.17.
 */

public class ShowTxtAssetActivity extends AppCompatActivity {

    private static String tag=ShowTxtAssetActivity.class.getSimpleName();

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.show_txtasset_activity);

            String fileName=getIntent().getExtras().getString(Constants.ASSET_NAME);

            AssetManager am = getApplicationContext().getAssets();
            TextView licenseText = (TextView) findViewById(R.id.licenseText);
            try {
                licenseText.setText(Utils.readInputStream(am.open(fileName+"x")));
            } catch (IOException e) {
                Log.e(tag,"\"Can't load \"+fileName+\" file)\"",e);
            }
        }
}
