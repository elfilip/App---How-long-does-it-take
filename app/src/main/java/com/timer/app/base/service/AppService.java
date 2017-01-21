package com.timer.app.base.service;

import android.util.Log;

import com.timer.app.base.entity.Action;
import com.timer.app.base.entity.Configuration;
import com.timer.app.base.entity.Status;
import com.timer.app.base.storage.ConfigurationStorage;
import com.timer.app.base.storage.Storage;

import java.util.List;

/**
 * Created by felias on 8.11.16.
 */

public class AppService {

    private static String tag=AppService.class.getSimpleName();


    private static AppService instance;
    private Storage storage;
    private ConfigurationStorage config;

    private AppService() {

    }

    public static AppService getInstance() {
        if (instance == null) {
            instance = new AppService();
        }
        return instance;
    }

    public void setStorage(Storage storage,ConfigurationStorage configStorage) {
        this.storage = storage;
        this.config=configStorage;
    }

    public boolean isStorageSet() {
        return this.storage!=null;
    }

    public Storage getStorage() {
        if(storage==null){
            RuntimeException fatalError=new RuntimeException("Storage has not been set. Please call method setStorage first");
            Log.e(tag,"Storage has not been set. Please call method setStorage first", fatalError);
            throw fatalError;
        }
        return storage;
    }

    private List<Action> loadActions() {
        return getStorage().loadActions();
    }

    public boolean addAction(Action action) {
        return getStorage().addAction(action);
    }

    public void saveActions() {
        getStorage().saveActions();
    }

    public List<Action> getActions() {
        return getStorage().getActions();
    }

    public void deleteAllActions() {
        getStorage().deleteAllActions();
    }

    public boolean deleteAction(String name) {
        return getStorage().deleteAction(name);
    }

    public void saveStatus(Status timerStatus) {
        getStorage().saveStatus(timerStatus);
    }

    public Status loadStatus() {
        return getStorage().loadStatus();
    }

    public boolean statusExist() {
        return getStorage().statusExist();

    }

    public void deleteStatus() {
        getStorage().deleteStatus();
    }

    public boolean checkIfActionExists(String name) {
        return getStorage().checkIfActionExists(name);
    }

    public Action getAction(String name) {
        return getStorage().getAction(name);
    }

    public void updateActionName(String actionName, String newName) {
        getStorage().updateActionName(actionName, newName);
    }

    public void updateActionNote(String actionName, int pos, String newNote) {
        getStorage().updateActionNote(actionName, pos, newNote);
    }

    public Configuration getConfig() {
        if (config == null) {
            Log.e(tag,"Config storage has not been set so it will not work", new RuntimeException("Config storage has not been set. Please call method setStorage first"));
            return new Configuration();
        }
        return config.loadConfig();
    }

    public void saveConfig(Configuration configuration) {
        config.saveConfig(configuration);
    }

}
