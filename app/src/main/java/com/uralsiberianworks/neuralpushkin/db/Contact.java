package com.uralsiberianworks.neuralpushkin.db;


import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Contact {
    @PrimaryKey @NonNull
    String contactID;
    String name;
    @DrawableRes int image;

    public Contact(@NonNull String contactID, String name, int image) {
        this.contactID = contactID;
        this.name = name;
        this.image = image;
    }

    public Contact() {

    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return contactID.equals(contact.contactID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactID, name, image);
    }
}
