package com.greimul.simpleflashcard

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.greimul.simpleflashcard.fragment.DeckFragment
import com.greimul.simpleflashcard.fragment.ErrorFragment
import com.greimul.simpleflashcard.fragment.InfoFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val PAGE_CNT = 2

    lateinit var  viewpager2: ViewPager2
    lateinit var  tablayout:TabLayout

    val tabLayoutTextArray = arrayOf("Deck","Info")
    val tabLayoutIconArray = arrayOf(R.drawable.ic_view_list_48px,R.drawable.ic_info_48px)

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

    private inner class ViewPagerAdapter(fa: FragmentActivity):FragmentStateAdapter(fa){
        override fun createFragment(position: Int): Fragment {
            when(position){
                0 -> return DeckFragment()
                1 -> return InfoFragment()
                else -> {
                    return ErrorFragment()
                }
            }
        }
        override fun getItemCount():Int = PAGE_CNT
    }

}
