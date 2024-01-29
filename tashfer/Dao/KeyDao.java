package com.najdat.tashfer.Dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.najdat.tashfer.Model.Key;

import java.util.List;

@Dao
public interface KeyDao {
    @Insert(onConflict = REPLACE)
    void saveItem(Key key);

    @Query("select * from keydb")
    List<Key> getAllKey();

    @Query("delete from keydb")
    Integer deleteAllKey();

    @Query("update keydb set key=:myKey where ID=:id")
    void changeKey(int id, String myKey);

    @Query("update keydb set messagebackup=:status where ID=:id")
    void enableDisable(int id, Boolean status);

    @Query("update keydb set security=:status where ID=:id")
    void enableDisableSecurity(int id, Boolean status);
}
