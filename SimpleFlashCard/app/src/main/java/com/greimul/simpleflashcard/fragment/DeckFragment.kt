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

class DeckFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var deckListAdapter:DeckListAdapter
    private lateinit var viewManager:RecyclerView.LayoutManager

    private lateinit var deckFab:FloatingActionButton

    private lateinit var deckViewModel:DeckViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.fragment_deck,container,false)

        deckViewModel = activity?.run{
            ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application))
                .get(DeckViewModel::class.java)
        }?:throw Exception("invalid")

        ////////////////////////////////////////////////////
        /*
        var testlist = mutableListOf<Deck>()
        for(i in 0..30)
            testlist.add(
                Deck(
                    1,
                    "asdf",
                    "asdf",
                    mutableListOf(
                        Card(
                            "asdf",
                            "asdf"
                        ),
                        Card("asdf", "asdf"),
                        Card("asdf", "asdf")
                    )
                )
            )
        */ ///////////////////////////////////////////////////

        deckListAdapter = DeckListAdapter(activity)
        viewManager = LinearLayoutManager(activity)
        deckViewModel.deckList.observe(this,
            Observer {
                    decks-> deckListAdapter.setDeck(decks)
            }
        )

        recyclerView = view.recyclerview_deck.apply{
            setHasFixedSize(true)
            adapter = deckListAdapter
            layoutManager = viewManager
            addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }

        deckFab = view.fab_deck
        deckFab.setOnClickListener {
            val dialog = AlertDialog.Builder(context,R.style.DialogStyle)
            val dialogView = layoutInflater.inflate(R.layout.dialog_new_deck,null)
            dialog.setView(dialogView).setPositiveButton("OK") {
                dialog,i->
                val deck = Deck(0,
                    dialogView.edittext_new_name.text.toString(),
                    dialogView.edittext_new_desc.text.toString(),0)
                deckViewModel.insert(deck)
            }.setNegativeButton("Cancle"){
                dialog,i->
            }.show()
        }

        return view
    }
}