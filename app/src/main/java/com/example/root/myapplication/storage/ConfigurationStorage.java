package com.example.root.myapplication.storage;

import com.example.root.myapplication.entity.Configuration;

/**
 * Created by elfilip on 12.1.17.
 */

public interface ConfigurationStorage {

    public Configuration loadConfig();

    public void saveConfig(Configuration configuration);

}
