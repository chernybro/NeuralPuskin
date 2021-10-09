package com.uralsiberianworks.neuralpushkin.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM chat WHERE chatID = :id") //  WHERE sender = :id
    Chat getChatFromID(String id);

    @Query("SELECT * FROM chat")
    List<Chat> getAllChats();

    @Query("SELECT CASE WHEN EXISTS ( SELECT * FROM chat WHERE chatID = :id) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
    boolean checkPushkinExist(String id);

    @Insert
    void insert(Chat chat);

    @Update
    void update(Chat chat);

}
