package com.uralsiberianworks.neuralpushkin.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Contact::class, parentColumns = ["contactID"], childColumns = ["chatID"], onDelete = ForeignKey.CASCADE)])
class Chat {
    @PrimaryKey
    var chatID: String? = null
    var sender: String? = null
    var lastMessage: String? = null
    var imagePath: String? = null

    constructor(chatID: String, sender: String?, lastMessage: String?, imagePath: String?) {
        this.chatID = chatID
        this.sender = sender
        this.lastMessage = lastMessage
        this.imagePath = imagePath
    }

    constructor() {}

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val chat = o as Chat
        return chatID == chat.chatID
    }
}