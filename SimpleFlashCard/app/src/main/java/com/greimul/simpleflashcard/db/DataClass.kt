package com.greimul.simpleflashcard.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Card(var front:String,var back:String)

@Entity(tableName = "deck_db")
data class Deck(@PrimaryKey(autoGenerate = true) val id:Int,
                @ColumnInfo(name = "name") var name:String,
                @ColumnInfo(name = "description") var description:String,
                @ColumnInfo(name = "card_list") val cardList:MutableList<Card>
)