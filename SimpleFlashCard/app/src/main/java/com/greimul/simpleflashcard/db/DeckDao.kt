package com.greimul.simpleflashcard.db

import androidx.room.*

@Dao
interface DeckDao {
    @Query("SELECT * FROM deck_db")
    fun getAll(): MutableList<Deck>

    @Query("SELECT * FROM deck_db WHERE id = :deckId")
    fun selectByID(deckId:Int):Deck

    @Query("DELETE FROM deck_db WHERE id = :deckId")
    fun deleteById(deckId: Int)

    @Query("SELECT * FROM card_db WHERE deckId = :deckId")
    fun loadCardsFromDeck(deckId:Int):List<Card>

    @Insert
    fun insertDeck(deck:Deck)

    @Insert
    fun insertCard(card:Card)

    @Delete
    fun deleteDeck(deck:Deck)
}