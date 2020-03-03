package com.greimul.simpleflashcard.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.greimul.simpleflashcard.db.Card
import com.greimul.simpleflashcard.db.Deck
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.ViewAdapter
import com.greimul.simpleflashcard.db.DeckDatabase
import kotlinx.android.synthetic.main.dialog_new_deck.view.*
import kotlinx.android.synthetic.main.fragment_deck.view.*
import kotlinx.coroutines.*

class DeckFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter:RecyclerView.Adapter<*>
    private lateinit var viewManager:RecyclerView.LayoutManager

    private lateinit var deckFab:FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val view = inflater.inflate(R.layout.fragment_deck,container,false)
        val dbDao = DeckDatabase.getDatabase(context!!).deckDao()
        val decklist = dbDao.getAll()

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

        viewAdapter = ViewAdapter(decklist)
        viewManager = LinearLayoutManager(activity)

        recyclerView = view.recyclerview_deck.apply{
            setHasFixedSize(true)
            adapter = viewAdapter
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
                    dialogView.edittext_new_desc.text.toString(),
                    mutableListOf())
                dbDao.insertDeck(deck)
                decklist.add(deck)
                viewAdapter.notifyItemInserted(decklist.size-1)
            }.setNegativeButton("Cancle"){
                dialog,i->
            }.show()
        }

        return view
    }
}