package com.greimul.simpleflashcard.db

import androidx.lifecycle.LiveData

class CardRepo(val cardDao:CardDao,val deckId:Int) {

    val cardList: LiveData<List<Card>> = cardDao.getCardsFromDeck(deckId)

    suspend fun insert(card:Card){
        cardDao.insertCard(card)
    }

    suspend fun delete(cardId:Int){
        cardDao.deleteCardById(cardId)
    }

}