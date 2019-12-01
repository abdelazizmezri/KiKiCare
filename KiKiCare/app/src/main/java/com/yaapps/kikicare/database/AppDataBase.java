package com.yaapps.kikicare.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.yaapps.kikicare.Entity.Animal;
import com.yaapps.kikicare.dao.AnimalDao;

@Database(entities = {Animal.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;

    public abstract AnimalDao animalDao();

    public static AppDataBase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "kiki_care_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

}
