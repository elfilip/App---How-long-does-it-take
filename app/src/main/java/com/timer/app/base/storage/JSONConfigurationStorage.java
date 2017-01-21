package com.timer.app.base.storage;

import android.util.Log;

import com.timer.app.base.entity.Configuration;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by elfilip on 12.1.17.
 */

public class JSONConfigurationStorage implements ConfigurationStorage {

    private final String FILENAME = "config.json";
    private static String tag=JSONConfigurationStorage.class.getSimpleName();

    private File rootNameDir;
    private Configuration configuration;

    public JSONConfigurationStorage(File rootNameDir) {
        this.rootNameDir = rootNameDir;
    }

    @Override
    public Configuration loadConfig() {
        if (configuration != null)
            return configuration;
        File file = new File(rootNameDir, FILENAME);
        try {
            if (!file.exists()) {
                configuration = new Configuration();
                saveConfig(configuration);
                return configuration;
            }
            FileReader fr = new FileReader(file);
            Gson g = new Gson();
            configuration = g.fromJson(fr, Configuration.class);
        } catch (Exception e) {
            Log.e(tag, "Can't load config",e);
            return new Configuration();
        }
        return configuration;
    }

    @Override
    public void saveConfig(Configuration configuration) {
        if (configuration == null)
            return;
        Gson g = new Gson();
        FileWriter f = null;
        try {
            String output = g.toJson(configuration);
            f = new FileWriter(new File(rootNameDir, FILENAME));
            f.write(output.toCharArray());
            f.flush();
        } catch (Exception e) {
            Log.e(tag, "Can't save config",e);
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    Log.e(tag, "Can't save config",e);
                }
            }
        }
    }
}
