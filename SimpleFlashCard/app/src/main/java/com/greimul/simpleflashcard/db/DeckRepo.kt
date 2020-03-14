package com.greimul.simpleflashcard.db

import androidx.lifecycle.LiveData

class DeckRepo(val deckDao:DeckDao){

    val deckList: LiveData<List<Deck>> = deckDao.getAll()

    fun countCards(deckId:Int):LiveData<Int> = deckDao.countCards(deckId)

    suspend fun insert(deck:Deck){
        deckDao.insertDeck(deck)
    }

    suspend fun delete(id:Int){
        deckDao.deleteDeckById(id)
    }

}