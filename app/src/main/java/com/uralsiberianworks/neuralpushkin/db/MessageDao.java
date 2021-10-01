package com.uralsiberianworks.neuralpushkin.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM message WHERE chatID = :chatID")
    List<Message> getAllMessages(String chatID);

    @Insert
    void insert(Message message);


    @Delete
    void delete(Message message);
}
