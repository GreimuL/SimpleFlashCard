package com.greimul.simpleflashcard.activity

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.greimul.simpleflashcard.db.Card
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.DeckPlayAdapter
import com.greimul.simpleflashcard.viewmodel.CardViewModel
import com.greimul.simpleflashcard.viewmodel.DeckViewModel
import kotlinx.android.synthetic.main.activity_deck_play.*
import kotlinx.coroutines.*
import java.lang.Math.abs

class DeckPlayActivity: AppCompatActivity() {

    private lateinit var cardViewModel:CardViewModel
    private lateinit var deckPlayAdapter: DeckPlayAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_play)

        val deckId = intent.getIntExtra("deckId",0)

        deckPlayAdapter = DeckPlayAdapter(this,seekbar_deck_play)

        cardViewModel = CardViewModel(application,deckId)
        cardViewModel.cardList.observe(this,
            Observer {
                    cards -> deckPlayAdapter.setCards(cards)
            }
        )

        val previewPx = resources.getDimension(R.dimen.viewpager2_preview)
        val pageMarginPx = resources.getDimension(R.dimen.viewpager2_page_margin)

        viewpager2_deck_play.apply{
            adapter = deckPlayAdapter
            clipToPadding = false
            offscreenPageLimit = 1
            setPageTransformer{
                page,position->
                page.translationX = -(previewPx+pageMarginPx)*position
                page.scaleY = 1-(0.25f*abs(position))
            }
            registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    seekbar_deck_play.progress = position
                }
            })
        }

        seekbar_deck_play.apply{
            max = 0
            setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    viewpager2_deck_play.currentItem = progress
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        button_bottom_left.setOnClickListener {
            viewpager2_deck_play.currentItem--
        }
        button_bottom_right.setOnClickListener {
            viewpager2_deck_play.currentItem++
        }
        button_bottom_right
        button_bottom_flip.setOnClickListener {
            deckPlayAdapter.fragment.flipCard()
        }
    }
}