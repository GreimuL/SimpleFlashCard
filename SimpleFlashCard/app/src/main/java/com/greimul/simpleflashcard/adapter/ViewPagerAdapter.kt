package com.greimul.simpleflashcard.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.greimul.simpleflashcard.fragment.DeckFragment
import com.greimul.simpleflashcard.fragment.ErrorFragment
import com.greimul.simpleflashcard.fragment.InfoFragment

class ViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa){

    val PAGE_CNT = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> DeckFragment()
            1 -> InfoFragment()
            2 -> InfoFragment()
            else -> ErrorFragment()
        }
    }

    override fun getItemCount():Int = PAGE_CNT
}