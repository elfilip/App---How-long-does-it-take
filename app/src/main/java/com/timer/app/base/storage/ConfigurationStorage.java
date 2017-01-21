package com.timer.app.base.storage;

import com.timer.app.base.entity.Configuration;

/**
 * Created by elfilip on 12.1.17.
 */

public interface ConfigurationStorage {

    public Configuration loadConfig();

    public void saveConfig(Configuration configuration);

}
