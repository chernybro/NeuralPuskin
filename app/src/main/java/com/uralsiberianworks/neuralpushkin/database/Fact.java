package com.uralsiberianworks.neuralpushkin.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(
        entity = Contact.class,
        parentColumns = "contactID",
        childColumns = "contactID"))
public class Fact {
    String fact;
    @PrimaryKey
    int factID;
    int contactID;
}
