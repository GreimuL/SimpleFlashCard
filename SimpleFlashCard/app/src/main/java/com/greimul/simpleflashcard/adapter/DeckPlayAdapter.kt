package com.greimul.simpleflashcard.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.greimul.simpleflashcard.Card
import com.greimul.simpleflashcard.fragment.CardFragment

class DeckPlayAdapter(fa: FragmentActivity, cardlist:MutableList<Card>): FragmentStateAdapter(fa) {

    val CARD_CNT = cardlist.size
    val cardlist=  cardlist

    override fun createFragment(position: Int): Fragment {
        return CardFragment(cardlist[position])
    }
    override fun getItemCount():Int=CARD_CNT
}