package com.uralsiberianworks.neuralpushkin.db;

import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("SELECT lastMessage FROM chat WHERE chatID = :id")
    String getLastMessageByID(String id);

    @Insert
    void insert(Chat chat);

    @Update
    void update(Chat chat);

    @Delete
    void delete(Chat chat);
}
