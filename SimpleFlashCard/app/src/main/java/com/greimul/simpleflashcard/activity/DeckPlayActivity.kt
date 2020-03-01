package com.greimul.simpleflashcard.activity

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.greimul.simpleflashcard.Card
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.DeckPlayAdapter
import kotlinx.android.synthetic.main.activity_deck_play.*
import java.lang.Math.abs

class DeckPlayActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_play)

        val listsize = intent.getIntExtra("size",0)

        val previewPx = resources.getDimension(R.dimen.viewpager2_preview)
        val pageMarginPx = resources.getDimension(R.dimen.viewpager2_page_margin)

        viewpager2_deck_play.apply{
            adapter = DeckPlayAdapter(this@DeckPlayActivity, mutableListOf(Card("asdf","asdfasf"),Card("asdf","asdfasf"),Card("asdf","asdfasf"),Card("asdf","asdfasf"),Card("asdf","asdfasf"),Card("asdf","asdfasf"),Card("asdf","asdfasf"),Card("asdf","asdfasf")))
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
            max = 7
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
    }
}