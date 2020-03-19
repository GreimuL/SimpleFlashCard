package com.greimul.simpleflashcard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greimul.simpleflashcard.db.Deck
import com.greimul.simpleflashcard.db.DeckDatabase
import com.greimul.simpleflashcard.db.DeckRepo
import kotlinx.coroutines.launch

class DeckViewModel(application:Application):AndroidViewModel(application) {

    private val repo:DeckRepo
    val deckList:LiveData<List<Deck>>
    var recentInsertedDeckId:LiveData<Long>
    init{
        repo = DeckRepo(DeckDatabase.getDatabase(application).deckDao())
        deckList = repo.deckList
        recentInsertedDeckId = repo.recentInsertedDeckId
    }

    fun insert(deck:Deck) = viewModelScope.launch {
        repo.insert(deck)
    }

    fun delete(deckId:Int) = viewModelScope.launch{
        repo.delete(deckId)
    }

    fun countCards(deckId: Int):LiveData<Int> = repo.countCards(deckId)
}