package com.uralsiberianworks.neuralpushkin.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Chat.class , Contact.class, Message.class}, version = 1)
public abstract class NeuralDatabase extends RoomDatabase {
    public abstract ChatDao getChatDao();
    public abstract ContactDao getContactDao();
    public abstract MessageDao getMessageDao();
}