package com.example.root.myapplication.service;

import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.Status;
import com.example.root.myapplication.storage.Storage;

import java.util.List;

/**
 * Created by felias on 8.11.16.
 */

public class AppService {

    private static AppService instance;
    private Storage storage;

    private AppService() {

    }

    public static AppService getInstance() {
        if (instance == null) {
            instance = new AppService();
        }
        return instance;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public boolean isStorageSet() {
        return this.storage!=null;
    }

    public Storage getStorage() {
        if(storage==null){
            throw new RuntimeException("Storage is not set. Please call method set storage first");
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

}
