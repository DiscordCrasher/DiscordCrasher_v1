package me.dc.automaticdiscordcrasher.jsonmanager;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonFileManager {


    File file;

    public JsonFileManager() {

        File theDir = new File("C:\\DiscordCrasher\\");


        if (!theDir.exists()) {
            try{
                theDir.mkdir();
            }
            catch(SecurityException se){
                se.printStackTrace();
            }
        }


        File file = new File("C:\\DiscordCrasher\\save_accounts.json");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }




            printWriter.print("[]");

            printWriter.flush();
            printWriter.close();
        }




        this.file = file;
    }

    public void saveUser(User user) {

        JSONArray jsonArray = new JSONArray();

        boolean add = true;

        for (User u : getUsers()) {
            if (user.getEmail().equalsIgnoreCase(u.getEmail())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", user.getEmail());
                jsonObject.put("password", user.getPassword());
                jsonArray.put(jsonObject);
                add = false;
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", u.getEmail());
                jsonObject.put("password", u.getPassword());
                jsonArray.put(jsonObject);
            }
        }

        if (add) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            jsonArray.put(jsonObject);
        }

        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        printWriter.print(jsonArray.toString());

        printWriter.flush();
        printWriter.close();
    }

    public boolean containsUser(String email) {
        for (User u : getUsers()) {
            if (email.equalsIgnoreCase(u.getEmail())) {
                return true;
            }
        }

        return false;
    }

    public User getUser(String email) {
        for (User u : getUsers()) {
            if (email.equalsIgnoreCase(u.getEmail())) {
                return u;
            }
        }

        return null;
    }

    public List<User> getUsers() {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        JSONTokener jsonTokener = new JSONTokener(stream);
        JSONArray jsonArray = new JSONArray(jsonTokener);
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<User> userList = new ArrayList<>();

        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;

            User user = new User(jsonObject.getString("email"), jsonObject.getString("password"));

            userList.add(user);
        }

        return userList;
    }

}
