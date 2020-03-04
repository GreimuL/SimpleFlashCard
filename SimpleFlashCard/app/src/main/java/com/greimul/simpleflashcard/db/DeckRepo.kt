package com.greimul.simpleflashcard.db

import androidx.lifecycle.LiveData
import kotlinx.coroutines.internal.synchronized

class DeckRepo(val deckDao:DeckDao){

    val deckList: LiveData<List<Deck>> = deckDao.getAll()

    suspend fun insert(deck:Deck){
        deckDao.insertDeck(deck)
    }
}