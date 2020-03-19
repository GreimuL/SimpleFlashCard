package com.greimul.simpleflashcard.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DeckRepo(val deckDao:DeckDao){

    val deckList: LiveData<List<Deck>> = deckDao.getAll()
    var recentInsertedDeckId:MutableLiveData<Long> = MutableLiveData()

    fun countCards(deckId:Int):LiveData<Int> = deckDao.countCards(deckId)

    suspend fun insert(deck:Deck){
        recentInsertedDeckId.postValue(deckDao.insertDeck(deck))
    }

    suspend fun delete(id:Int){
        deckDao.deleteDeckById(id)
    }

}