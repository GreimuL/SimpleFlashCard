package com.greimul.simpleflashcard.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var  viewpager2:ViewPager2
    lateinit var  tablayout:TabLayout

    val tabLayoutTextArray = arrayOf("Deck","Star","Info")
    val tabLayoutIconArray = arrayOf(
        R.drawable.ic_view_list_48px,
        R.drawable.ic_star_24px,
        R.drawable.ic_info_48px
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tablayout = tablayout_main

        viewpager2 = viewpager2_main
        viewpager2.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tablayout,viewpager2){tab,position->
            tab.text = tabLayoutTextArray[position]
            tab.setIcon(tabLayoutIconArray[position])
        }.attach()
    }
}
