package com.greimul.simpleflashcard.db

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(tableName = "deck_db")
data class Deck(@PrimaryKey(autoGenerate = true) val id:Int,
                @ColumnInfo(name = "name") var name:String,
                @ColumnInfo(name = "description") var description:String,
                @ColumnInfo(name = "size") var size:Int
)

@Entity(tableName = "card_db",
    foreignKeys = arrayOf(ForeignKey(entity = Deck::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("deckId"),
        onDelete = CASCADE)
    )
)
data class Card(@PrimaryKey val id:Int,
                @ColumnInfo(name = "front") var front:String,
                @ColumnInfo(name = "back") var back:String,
                val deckId:Int)