package com.uralsiberianworks.neuralpushkin.database;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Contact {
    @PrimaryKey @NonNull
    private String contactID;
    private String contactFacts;
    private String name;
    private String imagePath;

    public Contact(@NonNull String contactID, String contactFacts, String name, String imagePath) {
        this.contactID = contactID;
        this.contactFacts = contactFacts;
        this.name = name;
        this.imagePath = imagePath;
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

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getContactFacts() {
        return contactFacts;
    }

    public void setContactFacts(String contactFacts) {
        this.contactFacts = contactFacts;
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
        return Objects.hash(contactID, name, imagePath);
    }
}
