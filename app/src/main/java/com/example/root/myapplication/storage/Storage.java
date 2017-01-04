package com.example.root.myapplication.storage;

import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.Status;

import java.util.List;

/**
 * Created by felias on 13.12.16.
 */

public interface Storage {

    public List<Action> loadActions();

    public boolean addAction(Action action);

    public void saveActions();

    public List<Action> getActions();

    public void deleteAllActions();

    public boolean deleteAction(String name);

    public void saveStatus(Status timerStatus);

    public Status loadStatus();

    public boolean statusExist();

    public void deleteStatus();

    public boolean checkIfActionExists(String name);

    public Action getAction(String name);

    public void updateActionName(String actionName, String newName);

    public void updateActionNote(String actionName, int pos, String newNote);

}
