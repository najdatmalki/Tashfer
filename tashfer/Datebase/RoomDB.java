package com.najdat.tashfer.Datebase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.najdat.tashfer.Dao.KeyDao;
import com.najdat.tashfer.Dao.MessageDao;
import com.najdat.tashfer.Model.Key;
import com.najdat.tashfer.Model.Message;

@Database(entities = {Message.class, Key.class},version = 1,exportSchema = false)
public abstract class  RoomDB extends RoomDatabase {

    private static RoomDB datebase;
    private static  String DATABASE_NAME="tashferDb";

    public synchronized static RoomDB getInstance(Context context){
        if (datebase == null){
            datebase = Room.databaseBuilder(context.getApplicationContext(),RoomDB.class,DATABASE_NAME)
            .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return datebase;
    }

    public abstract MessageDao mainDao();
    public abstract KeyDao keyDao();

}
