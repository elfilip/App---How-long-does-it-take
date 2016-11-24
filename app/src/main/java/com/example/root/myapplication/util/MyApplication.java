package com.example.root.myapplication.util;

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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by felias on 8.11.16.
 */

public class MyApplication {

    private static MyApplication instance;
    private final String statusFileName = "status.properties";
    private final String timerBaseProp = "timerBase";
    private final String actionNameProp = "actionName";
    private List<Action> actions;
    private File rootName;
    private Properties status = new Properties();
    private MyApplication() {

    }

    public static MyApplication getInstance(File root) {
        if (instance == null) {
            instance = new MyApplication();
            instance.rootName = root;
        }
        return instance;
    }

    private List<Action> loadActions() {
        File file = new File(rootName, Constants.ACTION_FILE);
        List<Action> result = null;
        try {
            if (!file.exists()) {

                file.createNewFile();

            }
            readFile(file);
            FileReader fr = new FileReader(file);
            Gson g = new Gson();
            result = g.fromJson(fr, new TypeToken<List<Action>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
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
            f = new FileWriter(new File(rootName, Constants.ACTION_FILE));
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

            System.out.println("After reading file");
            System.out.println(str);
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
        actions = new LinkedList<>();
        Gson g = new Gson();
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
        File file = new File(rootName, Constants.STATUS_FILE);
        status.put(actionNameProp, timerStatus.getActionName());
        status.put(timerBaseProp, String.valueOf(timerStatus.getTimerBase()));
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
        File file = new File(rootName, Constants.STATUS_FILE);
        if(file.exists()==false){
            return null;
        }
        Properties status = new Properties();
        FileInputStream is = null;
        Status timerStatus=null;
        try {
            is = new FileInputStream(file);
            status.load(is);

        timerStatus = new Status((String) status.getProperty(actionNameProp), Long.parseLong((String)status.get(timerBaseProp)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timerStatus;
    }

    public void deleteStatusFile() {
        File file = new File(rootName, Constants.STATUS_FILE);
        file.delete();
        status.clear();
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

    public void updateActionNote(String actionName, String newNote){
        Action a = getAction(actionName);
        if (a == null) {
            throw new RuntimeException("Updating unknown action");
        }
        a.setNote(newNote);
        saveActions();
    }

}
