package com.uralsiberianworks.neuralpushkin.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Chat::class, parentColumns = ["chatID"], childColumns = ["chatID"], onDelete = ForeignKey.CASCADE)])
class Message {
    @PrimaryKey
    var messageID: String? = null
    var text: String? = null
    var chatID: String? = null
    var type: String? = null
    var initialLength = 0

}