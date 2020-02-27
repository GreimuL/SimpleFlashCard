package com.greimul.simpleflashcard

data class Card(var front:String,var back:String)
data class Deck(var name:String,var description:String,val cardList:MutableList<Card>,var mark:Boolean)