package com.example.root.myapplication.storage;

import com.example.root.myapplication.entity.Action;
import com.example.root.myapplication.entity.Status;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by felias on 13.12.16.
 */

public class FileStorage implements Storage{



    private final String TIMER_PROP_NAME = "timerBase";
    private final String ACTION_NAME_PROP_NAME = "actionName";
    private final String REQ_CODE_PROP_NAME = "request_code";
    private final String NOTE_PROP_NAME = "note";
    private final String ACTION_FILE="actions.json";
    private final String STATUS_FILE="status.properties";

    private List<Action> actions;
    private File rootNameDir;


    public FileStorage(File rootNameDir) {
        this.rootNameDir =rootNameDir;
    }

    public List<Action> loadActions() {
        File file = new File(rootNameDir, ACTION_FILE);
        List<Action> result = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            //file.delete();
            readFile(file);
            FileReader fr = new FileReader(file);
            Gson g = new Gson();
            result = g.fromJson(fr, new TypeToken<List<Action>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
        if (result == null)
            return new LinkedList<>();
        else
            return result;

    }

    public boolean addAction(Action action) {
        if (actions == null)
            actions = loadActions();
        actions.add(action);
        saveActions();
        return true;
    }

    public void saveActions() {
        if (actions == null)
            return;
        Gson g = new Gson();
        FileWriter f = null;
        try {
            String output = g.toJson(actions);
            f = new FileWriter(new File(rootNameDir, ACTION_FILE));
            f.write(output.toCharArray());
            f.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Action> getActions() {
        if (actions == null) {
            actions = loadActions();
        }
        if (actions != null) {
            return actions;
        } else {
            return new LinkedList<>();
        }
    }

    public String readFile(File file) {
        FileInputStream fis = null;
        String str = "";

        try {
            fis = new FileInputStream(file);
            int content;
            while ((content = fis.read()) != -1) {
                // convert to char and display it
                str += (char) content;
            }
            return str;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public void deleteAllActions() {
        actions.clear();
        saveActions();
    }

    public boolean deleteAction(String name) {
        if (actions == null) {
            return false;
        }
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).getName().equals(name)) {
                actions.remove(i);
                saveActions();
                return true;
            }
        }
        return false;
    }

    public void saveStatus(Status timerStatus) {
        Properties status = new Properties();
        File file = new File(rootNameDir, STATUS_FILE);
        status.put(ACTION_NAME_PROP_NAME, timerStatus.getActionName());
        status.put(TIMER_PROP_NAME, String.valueOf(timerStatus.getTimerBase()));
        status.put(REQ_CODE_PROP_NAME, String.valueOf(timerStatus.getRequestCode()));
        if(timerStatus.getNote()!=null) {
            status.put(NOTE_PROP_NAME, timerStatus.getNote());
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            status.store(os, "Status properties");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Status loadStatus() {
        File file = new File(rootNameDir, STATUS_FILE);
        if(file.exists()==false){
            return null;
        }
        Properties status = new Properties();
        FileInputStream is = null;
        Status timerStatus=null;
        try {
            is = new FileInputStream(file);
            status.load(is);

            timerStatus = new Status((String) status.getProperty(ACTION_NAME_PROP_NAME),
                    Long.parseLong((String)status.get(TIMER_PROP_NAME)),
                    Integer.parseInt(status.getProperty(REQ_CODE_PROP_NAME)));
            timerStatus.setNote(status.getProperty(NOTE_PROP_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timerStatus;
    }

    public boolean statusExist() {
        return (new File(rootNameDir,STATUS_FILE)).exists();
    }

    public void deleteStatus() {
        File file = new File(rootNameDir, STATUS_FILE);
        file.delete();
    }

    public boolean checkIfActionExists(String name) {
        for (Action a : actions) {
            if (a.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Action getAction(String name) {
        for (Action a : actions) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    public void updateActionName(String actionName, String newName) {
        Action a = getAction(actionName);
        if (a == null) {
            throw new RuntimeException("Updating unknown action");
        }
        a.setName(newName);
        saveActions();
    }

    public void updateActionNote(String actionName, int pos, String newNote){
        Action a = getAction(actionName);
        if (a == null) {
            throw new RuntimeException("Updating unknown action");
        }
        a.getMeasurement().get(pos).setNote(newNote);
        saveActions();
    }

}
