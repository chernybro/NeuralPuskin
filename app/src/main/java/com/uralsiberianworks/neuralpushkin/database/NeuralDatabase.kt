package com.uralsiberianworks.neuralpushkin.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Chat::class, Contact::class, Message::class], version = 1)
abstract class NeuralDatabase : RoomDatabase() {
    abstract val chatDao: ChatDao?
    abstract val contactDao: ContactDao?
    abstract val messageDao: MessageDao?
}