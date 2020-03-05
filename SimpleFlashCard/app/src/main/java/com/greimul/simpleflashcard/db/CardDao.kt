package com.greimul.simpleflashcard.db

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query

interface CardDao {
    @Query("SELECT * FROM card_db")
    fun getAll():LiveData<List<Card>>

    @Query("SELECT * FROM card_db WHERE id = :cardId")
    fun selectCardById(cardId:Int)

    @Query("SELECT * FROM card_db WHERE deckId = :deckId")
    fun loadCardsFromDeck(deckId:Int):List<Card>

    @Query("DELETE FROM card_db WHERE id = :cardId")
    suspend fun deleteCardById(cardId:Int)

    @Insert
    suspend fun insertCard(card:Card)
}