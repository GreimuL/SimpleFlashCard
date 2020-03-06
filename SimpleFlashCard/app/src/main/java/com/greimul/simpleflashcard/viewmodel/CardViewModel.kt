package com.greimul.simpleflashcard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.greimul.simpleflashcard.db.Card
import com.greimul.simpleflashcard.db.CardRepo
import com.greimul.simpleflashcard.db.DeckDatabase
import kotlinx.coroutines.launch

class CardViewModel(application: Application,deckId:Int):AndroidViewModel(application) {

    private val repo: CardRepo
    val cardList: LiveData<List<Card>>

    init{
        repo = CardRepo(DeckDatabase.getDatabase(application).cardDao(),deckId)
        cardList = repo.cardList
    }

    fun insert(card:Card) = viewModelScope.launch {
        repo.insert(card)
    }

    fun delete(cardId:Int) = viewModelScope.launch {
        repo.delete(cardId)
    }

}