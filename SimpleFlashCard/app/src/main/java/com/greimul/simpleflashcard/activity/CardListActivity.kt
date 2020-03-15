package com.greimul.simpleflashcard.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
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
import kotlinx.android.synthetic.main.dialog_new_card.view.*

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
            val dialog = AlertDialog.Builder(this,R.style.Theme_MaterialComponents_DayNight_NoActionBar_Bridge)
            val dialogView = layoutInflater.inflate(R.layout.dialog_new_card,null)
            dialogView.toolbar_new_card.apply{
                setTitle("New Card")
            }
            dialog.setView(dialogView).show()
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