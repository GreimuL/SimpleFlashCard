package com.greimul.simpleflashcard.adapter

import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.greimul.simpleflashcard.db.Card
import com.greimul.simpleflashcard.fragment.CardFragment

class AdapterTest(fa: FragmentActivity,seekBar: SeekBar,button:Button): FragmentStateAdapter(fa) {

    private var viewData = listOf<Card>()
    private val seekBar = seekBar
    private val button = button
    lateinit var fragment:CardFragment

    override fun createFragment(position: Int): Fragment {
        fragment = CardFragment(viewData[position],false,button)
        return fragment
    }

    override fun getItemCount():Int=viewData.size

    fun setCards(cardList:List<Card>){
        viewData = cardList
        notifyDataSetChanged()
        seekBar.max = viewData.size-1
    }
}