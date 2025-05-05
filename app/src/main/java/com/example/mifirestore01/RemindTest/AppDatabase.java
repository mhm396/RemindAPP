package com.example.mifirestore01.RemindTest;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Reminders.class} ,version = 2)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE = null;

    public abstract RoomDAO getRoomDAO();

    public static AppDatabase geAppdatabase(Context context){

        if(INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"reminds")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;

    }

    public static void destroyInstance(){
        INSTANCE = null;
    }

}
