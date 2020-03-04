package com.greimul.simpleflashcard.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DeckDao {
    @Query("SELECT * FROM deck_db")
    fun getAll(): LiveData<List<Deck>>

    @Query("SELECT * FROM deck_db WHERE id = :deckId")
    fun selectByID(deckId:Int):Deck

    @Query("DELETE FROM deck_db WHERE id = :deckId")
    suspend fun deleteById(deckId: Int)

    @Query("SELECT * FROM card_db WHERE deckId = :deckId")
    fun loadCardsFromDeck(deckId:Int):List<Card>

    @Insert
    suspend fun insertDeck(deck:Deck)

    @Insert
    suspend fun insertCard(card:Card)

    @Delete
    suspend fun deleteDeck(deck:Deck)
}