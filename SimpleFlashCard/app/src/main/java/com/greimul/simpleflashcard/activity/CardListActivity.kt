package com.greimul.simpleflashcard.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.CardListAdapter
import com.greimul.simpleflashcard.db.Card
import kotlinx.android.synthetic.main.activity_card_list.*

class CardListActivity:AppCompatActivity() {

    private lateinit var recyclerview:RecyclerView
    private lateinit var cardListAdapter: CardListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)

        cardListAdapter = CardListAdapter()
        viewManager = GridLayoutManager(applicationContext,2)

        cardListAdapter.setCards(listOf(
            Card(1,"asdf","asdf",1),
            Card(1,"asdf","asdf",1),
            Card(1,"asdf","asdf",1),
            Card(1,"asdf","asdf",1),
            Card(1,"asdf","asdf",1)
        ))

        recyclerview = recyclerview_card.apply{
            setHasFixedSize(true)
            adapter = cardListAdapter
            layoutManager = viewManager
        }
    }
}