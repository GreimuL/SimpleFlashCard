package com.greimul.simpleflashcard.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.greimul.simpleflashcard.db.Deck
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.DeckListAdapter
import com.greimul.simpleflashcard.viewmodel.DeckViewModel
import kotlinx.android.synthetic.main.dialog_new_deck.view.*
import kotlinx.android.synthetic.main.fragment_deck.view.*
import java.lang.Exception

class DeckFragment(val deckViewModel:DeckViewModel): Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var deckListAdapter:DeckListAdapter
    private lateinit var viewManager:RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.fragment_deck,container,false)

        deckListAdapter = DeckListAdapter(activity)
        deckViewModel.deckList.observe(this,
            Observer {
                    decks-> deckListAdapter.setDeck(decks)
            }
        )

        viewManager = LinearLayoutManager(activity)

        recyclerView = view.recyclerview_deck.apply{
            setHasFixedSize(true)
            adapter = deckListAdapter
            layoutManager = viewManager
            addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }

        return view
    }
}