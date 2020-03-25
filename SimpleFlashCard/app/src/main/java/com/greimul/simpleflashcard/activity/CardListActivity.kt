package com.greimul.simpleflashcard.activity

import android.animation.Animator
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

class CardListActivity:AppCompatActivity() {

    private lateinit var recyclerView:RecyclerView
    private lateinit var cardListAdapter: CardAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var cardViewModel:CardViewModel

    var isAllFlip:Boolean = false

    var isFabClick:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)
        linearlayout_card_list.visibility = View.INVISIBLE
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
            if(!isFabClick) {
                var openAddCardAnimation = ViewAnimationUtils.createCircularReveal(linearlayout_card_list,coordinatorlayout_card_list.right,coordinatorlayout_card_list.bottom,0f,
                    hypot(coordinatorlayout_card_list.width.toDouble(),coordinatorlayout_card_list.height.toDouble()).toFloat())
                isFabClick = true
                linearlayout_card_list.visibility = View.VISIBLE
                openAddCardAnimation.addListener(object:Animator.AnimatorListener{
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        recyclerview_card.visibility = View.INVISIBLE
                        fab_card.setImageResource(R.drawable.ic_close_48px)
                        fab_card.show()
                    }
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationStart(animation: Animator?) {}
                })
                fab_card.hide()
                openAddCardAnimation.start()
            }
            else{
                var closeAddCardAnimation = ViewAnimationUtils.createCircularReveal(linearlayout_card_list,coordinatorlayout_card_list.right,coordinatorlayout_card_list.bottom,
                    Math.max(linearlayout_card_list.width,linearlayout_card_list.height).toFloat(),
                    0f)
                isFabClick = false
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
                recyclerview_card.visibility = View.VISIBLE
            }
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