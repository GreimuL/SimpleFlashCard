package com.greimul.simpleflashcard.db

import androidx.lifecycle.LiveData

class CardRepo(val cardDao:CardDao) {

    val cardList: LiveData<List<Card>> = cardDao.getAll()
}