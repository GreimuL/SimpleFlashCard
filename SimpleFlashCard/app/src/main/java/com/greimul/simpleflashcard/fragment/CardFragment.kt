package com.greimul.simpleflashcard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.greimul.simpleflashcard.db.Card
import com.greimul.simpleflashcard.R
import kotlinx.android.synthetic.main.item_card_play.view.*

class CardFragment(card: Card,isFlip:Boolean,flipButton:Button): Fragment(){
    ////////////////////////////////////////////
    ////////////////////////////////////////////CardFragment is Test Code
    val card = card
    var flip:Boolean = isFlip
    val button = flipButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.item_card_play,container,false)
        view.textview_play_card_text.text = card.front

        view.cardview_play_card.setOnClickListener {
            flip = !flip
            if(flip)
                view.textview_play_card_text.text = card.back
            else
                view.textview_play_card_text.text = card.front
        }
        button.setOnClickListener{
            flip = !flip
            if(flip)
                view.textview_play_card_text.text = card.back
            else
                view.textview_play_card_text.text = card.front
        }
        return view
    }

    fun flipCard(){

    }
}