package com.uralsiberianworks.neuralpushkin.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Contact {
    @PrimaryKey
    var contactID: String? = null
    var name: String? = null
    var imagePath: String? = null

    constructor(contactID: String, name: String?, imagePath: String?) {
        this.contactID = contactID
        this.name = name
        this.imagePath = imagePath
    }

    constructor() {}

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val contact = o as Contact
        return contactID == contact.contactID
    }

    override fun hashCode(): Int {
        return Objects.hash(contactID, name, imagePath)
    }
}