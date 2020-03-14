package com.greimul.simpleflashcard.activity

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.CardAdapter
import com.greimul.simpleflashcard.db.Card
import com.greimul.simpleflashcard.viewmodel.CardViewModel
import kotlinx.android.synthetic.main.activity_card_list.*
import kotlinx.android.synthetic.main.fragment_new_card.view.*

class CardListActivity:AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var cardListAdapter: CardAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var cardViewModel:CardViewModel

    var isAllFlip:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)

        val deckId = intent.getIntExtra("deckId",0)

        cardViewModel = ViewModelProvider(this,
                object:ViewModelProvider.Factory{
                    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                        return CardViewModel(application,deckId) as T
                    }
                })
                .get(CardViewModel::class.java)

        cardListAdapter = CardAdapter(null,0)
        viewManager = GridLayoutManager(applicationContext,2)

        cardViewModel.cardList.observe(this,
            Observer {
                    cards->cardListAdapter.setCards(cards)
            }
        )

        recyclerView = recyclerview_card.apply{
            setHasFixedSize(true)
            adapter = cardListAdapter
            layoutManager = viewManager
        }

        fab_card.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.fragment_new_card,null)
            dialog.setView(dialogView).setPositiveButton("OK") { dialog,i->
                cardViewModel.insert(
                    Card(0,dialogView.edittext_front.text.toString(),
                        dialogView.edittext_back.text.toString(),
                        deckId)
                )
            }.setNegativeButton("Cancle"){dialog,i->
            }.show()
                //recyclerView.scrollToPosition(cardListAdapter.itemCount)
        }

        button_flip_card_list.setOnClickListener {
            isAllFlip = !isAllFlip
            if(isAllFlip)
                button_flip_card_list.text = "All: Back"
            else
                button_flip_card_list.text = "All: Front"
            cardListAdapter.flipAllCards()
        }
    }
}