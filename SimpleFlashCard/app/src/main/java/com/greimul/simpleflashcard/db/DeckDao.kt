package com.greimul.simpleflashcard.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DeckDao {
    @Query("SELECT * FROM deck_db")
    fun getAll(): MutableList<Deck>

    @Query("SELECT * FROM deck_db WHERE id = :deckId")
    fun selectByID(deckId:Int):Deck

    @Query("DELETE FROM deck_db WHERE id = :deckId")
    fun deleteById(deckId: Int)

    @Insert
    fun insertDeck(deck:Deck)

    @Delete
    fun deleteDeck(deck:Deck)
}