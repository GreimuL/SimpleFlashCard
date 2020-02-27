package com.greimul.simpleflashcard.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.greimul.simpleflashcard.Card
import com.greimul.simpleflashcard.Deck
import com.greimul.simpleflashcard.MainActivity
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.ViewAdapter
import kotlinx.android.synthetic.main.dialog_new_deck.*
import kotlinx.android.synthetic.main.fragment_deck.*
import kotlinx.android.synthetic.main.fragment_deck.view.*
import kotlinx.android.synthetic.main.item_deck.view.*
import org.w3c.dom.Text

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

        /////////////////////////////////////////////////////
        var testlist = mutableListOf<Deck>()
        for(i in 0..30)
            testlist.add(Deck("asdf","asdf", mutableListOf(Card("asdf","asdf")),false))
        /////////////////////////////////////////////////////

        viewAdapter = ViewAdapter(testlist)
        viewManager = LinearLayoutManager(activity)

        recyclerView = view.recyclerview_deck.apply{
            setHasFixedSize(true)
            adapter = viewAdapter
            layoutManager = viewManager
        }
        deckFab = view.fab_deck
        deckFab.setOnClickListener {
            val dialog = AlertDialog.Builder(activity)
            val dialogView = layoutInflater.inflate(R.layout.dialog_new_deck,null)
            dialog.setView(dialogView).setPositiveButton("OK") {
                dialog,i->
            }.setNegativeButton("Cancle"){
                dialog,i->
            }.show()
        }


        return view
    }
}