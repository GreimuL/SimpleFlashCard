package com.greimul.simpleflashcard.activity

import android.animation.Animator
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.*
import android.view.View.inflate
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.animation.addListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.CardAdapter
import com.greimul.simpleflashcard.db.Card
import com.greimul.simpleflashcard.viewmodel.CardViewModel
import kotlinx.android.synthetic.main.activity_card_list.*
import kotlinx.android.synthetic.main.dialog_new_card.*
import kotlinx.android.synthetic.main.dialog_new_card.view.*
import kotlinx.android.synthetic.main.dialog_new_card.view.toolbar_new_card
import kotlin.math.hypot
import kotlin.properties.Delegates

class CardListActivity:AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var cardListAdapter: CardAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var cardViewModel:CardViewModel

    private var deckId:Int = 0

    var isAllFlip:Boolean = false

    var isAddCardOpen:Boolean = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(!isAddCardOpen)
            menuInflater.inflate(R.menu.menu_card_list,menu)
        else
            menuInflater.inflate(R.menu.menu_card_list_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                closeAddCard()
                return true
            }
            R.id.menu_card_list_flip -> {
                cardListAdapter.flipAllCards()
                return true
            }
            R.id.menu_card_list_add->{
                cardViewModel.insert(Card(0,edittext_card_add_front.text.toString(),edittext_card_add_back.text.toString(),deckId))
                closeAddCard()
                return true
            }

            else-> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)
        setSupportActionBar(toolbar_card_list)


        linearlayout_card_list.visibility = View.INVISIBLE
        deckId = intent.getIntExtra("deckId",0)

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
            isAddCardOpen = true
            setSupportActionBar(toolbar_card_list_add)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_48px)

            var openAddCardAnimation = ViewAnimationUtils.createCircularReveal(
                linearlayout_card_list,
                coordinatorlayout_card_list.right,
                coordinatorlayout_card_list.bottom,
                0f,
                hypot(
                    coordinatorlayout_card_list.width.toDouble(),
                    coordinatorlayout_card_list.height.toDouble()
                ).toFloat()
            )
            linearlayout_card_list.visibility = View.VISIBLE
            openAddCardAnimation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    recyclerview_card.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}
            })
            fab_card.hide()
            openAddCardAnimation.start()

            /*
            val dialog = AlertDialog.Builder(this,R.style.DialogFullScreen)
            val dialogView = layoutInflater.inflate(R.layout.dialog_new_card,null)

            dialogView.toolbar_new_card.apply{
                title = "New Card"
                setNavigationIcon(R.drawable.ic_close_48px)
                setNavigationOnClickListener {

                }
            }
            dialog.setOnDismissListener {
                recyclerview_card.visibility = View.VISIBLE
                fab_card.show()
            }
            dialog.setView(dialogView).show()
                //recyclerView.scrollToPosition(cardListAdapter.itemCount)

             */
        }
    }

    fun closeAddCard(){
        isAddCardOpen = false
        setSupportActionBar(toolbar_card_list)
        var closeAddCardAnimation = ViewAnimationUtils.createCircularReveal(linearlayout_card_list,coordinatorlayout_card_list.right,coordinatorlayout_card_list.bottom,
            Math.max(linearlayout_card_list.width,linearlayout_card_list.height).toFloat(),
            0f)
        closeAddCardAnimation.addListener(object:Animator.AnimatorListener{
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                linearlayout_card_list.visibility = View.INVISIBLE
                fab_card.setImageResource(R.drawable.ic_add_48px)
                fab_card.show()
            }
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
        fab_card.hide()
        closeAddCardAnimation.start()
        edittext_card_add_back.text.clear()
        edittext_card_add_front.text.clear()
        recyclerview_card.visibility = View.VISIBLE
    }
}