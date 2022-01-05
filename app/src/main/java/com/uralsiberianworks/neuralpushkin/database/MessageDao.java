package com.uralsiberianworks.neuralpushkin.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM message WHERE chatID = :chatID")
    List<Message> getAllMessages(String chatID);

    @Query("SELECT text FROM message WHERE chatID = :chatID")
    List<String> getAllHistory(String chatID);

    @Insert
    void insert(Message message);

    @Delete
    void delete(Message message);
}
