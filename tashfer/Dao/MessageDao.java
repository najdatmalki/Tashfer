package com.najdat.tashfer.Dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.najdat.tashfer.Model.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert(onConflict = REPLACE)
    void saveItems(Message message);

    @Query("select * from message order by creationtime desc")
    List<Message> getAllMessage();

    @Delete
    void delete(Message message);

    @Query("delete from message")
    void deleteAllMessages();
}
