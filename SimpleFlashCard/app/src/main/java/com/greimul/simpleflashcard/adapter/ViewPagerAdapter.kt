package com.greimul.simpleflashcard.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.greimul.simpleflashcard.fragment.*
import com.greimul.simpleflashcard.viewmodel.DeckViewModel

class ViewPagerAdapter(fa: FragmentActivity,val deckViewModel:DeckViewModel): FragmentStateAdapter(fa){

    val PAGE_CNT = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> DeckFragment(deckViewModel)
            1 -> ImExportFragment(deckViewModel)
            2 -> InfoFragment()
            else -> ErrorFragment()
        }
    }

    override fun getItemCount():Int = PAGE_CNT
}