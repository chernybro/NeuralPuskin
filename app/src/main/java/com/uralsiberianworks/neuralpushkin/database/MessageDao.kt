package com.uralsiberianworks.neuralpushkin.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM message WHERE chatID = :chatID")
    fun getAllMessages(chatID: String?): MutableList<Message?>?

    @Insert
    fun insert(message: Message?)

    @Delete
    fun delete(message: Message?)
}