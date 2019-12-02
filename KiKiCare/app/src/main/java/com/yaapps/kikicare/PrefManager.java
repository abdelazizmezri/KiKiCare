package com.yaapps.kikicare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.yaapps.kikicare.Entity.User;

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = "start_welcome";


    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean("IsFirstTimeLaunch", isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean("IsFirstTimeLaunch", true);
    }

    public void setUser(User user) {
        if(user!=null){
            editor.putInt("idUser", user.getId());
            editor.putString("email", user.getEmail());
            editor.putString("first_name", user.getFirstName());
            editor.putString("last_name", user.getLastName());
            editor.putString("password", user.getPassword());
            editor.putString("url_image", user.getUrlImage());
            editor.putString("mode", user.getMode());
        }else{
            editor.putInt("idUser", 0);
            editor.putString("email", "");
            editor.putString("first_name", "");
            editor.putString("last_name", "");
            editor.putString("password", "");
            editor.putString("url_image", "");
            editor.putString("mode", "");
        }
        editor.commit();
    }

    public User getUser() {
        if(pref.getString("email","").isEmpty())
            return null;
        else{
            return new User(
                    pref.getInt("idUser", 0),
                    pref.getString("email",""),
                    pref.getString("first_name",""),
                    pref.getString("last_name",""),
                    pref.getString("password",""),
                    pref.getString("url_image",""),
                    pref.getString("mode","")
            );
        }
    }
}