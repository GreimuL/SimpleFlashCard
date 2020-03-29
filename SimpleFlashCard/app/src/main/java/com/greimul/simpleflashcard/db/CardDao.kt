package com.greimul.simpleflashcard.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardDao {
    @Query("SELECT * FROM card_db")
    fun getAll():LiveData<List<Card>>

    @Query("SELECT * FROM card_db WHERE id = :cardId")
    fun selectCardById(cardId:Int):LiveData<Card>

    @Query("SELECT * FROM card_db WHERE deckId = :deckId")
    fun getCardsFromDeck(deckId:Int):LiveData<List<Card>>

    @Query("DELETE FROM card_db WHERE id = :cardId")
    suspend fun deleteCardById(cardId:Int)

    @Query("UPDATE card_db SET front = :front,back = :back WHERE id = :id")
    suspend fun updateCard(id:Int,front:String,back:String)

    @Insert
    suspend fun insertCard(card:Card)
}