package com.uralsiberianworks.neuralpushkin.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contact WHERE contactID = :id")
    Contact getContact(String id);

    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);


    @Query("DELETE FROM contact WHERE contactID = :id")
    void del(String id);
}
