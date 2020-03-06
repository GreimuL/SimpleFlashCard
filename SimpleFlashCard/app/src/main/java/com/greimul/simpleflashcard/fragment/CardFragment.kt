package com.greimul.simpleflashcard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.greimul.simpleflashcard.db.Card
import com.greimul.simpleflashcard.R
import kotlinx.android.synthetic.main.fragment_card.*
import kotlinx.android.synthetic.main.fragment_card.view.*
import kotlinx.android.synthetic.main.fragment_card.view.cardview_play_card

class CardFragment(card: Card): Fragment(){
    val card = card
    var flip:Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.fragment_card,container,false)
        view.textview_play_card_text.text = card.front

        view.cardview_play_card.setOnClickListener {
            flip = !flip
            if(flip)
                view.textview_play_card_text.text = card.back
            else
                view.textview_play_card_text.text = card.front
        }
        return view
    }
}