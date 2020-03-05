package com.greimul.simpleflashcard.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DeckDao {
    @Query("SELECT * FROM deck_db")
    fun getAll(): LiveData<List<Deck>>

    @Query("SELECT * FROM deck_db WHERE id = :deckId")
    fun selectDeckByID(deckId:Int):Deck

    @Query("DELETE FROM deck_db WHERE id = :deckId")
    suspend fun deleteDeckById(deckId: Int)

    @Insert
    suspend fun insertDeck(deck:Deck)
}