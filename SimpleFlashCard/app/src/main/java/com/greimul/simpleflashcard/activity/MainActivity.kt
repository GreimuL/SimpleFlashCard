package com.greimul.simpleflashcard.activity

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.ViewPagerAdapter
import com.greimul.simpleflashcard.db.Deck
import com.greimul.simpleflashcard.viewmodel.CardViewModel
import com.greimul.simpleflashcard.viewmodel.DeckViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_new_deck.view.*

class MainActivity : AppCompatActivity() {

    lateinit var  viewpager2:ViewPager2
    lateinit var  tablayout:TabLayout
    private lateinit var deckFab: FloatingActionButton
    private lateinit var deckViewModel: DeckViewModel
    private lateinit var cardViewModel: CardViewModel
    val tabLayoutTextArray = arrayOf("Deck","Im/Export","Info")
    val tabLayoutIconArray = arrayOf(
        R.drawable.ic_view_list_48px,
        R.drawable.ic_import_export_48px,
        R.drawable.ic_info_48px
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        deckViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(DeckViewModel::class.java)

        cardViewModel = ViewModelProvider(this,
            object:ViewModelProvider.Factory{
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return CardViewModel(application,-1) as T
                }
            })
            .get(CardViewModel::class.java)

        tablayout = tablayout_main
        tablayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab==null)
                    fab_main.hide()
                else
                    if(tab.position==0)
                        fab_main.show()
                    else
                        fab_main.hide()
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        viewpager2 = viewpager2_main
        viewpager2.adapter = ViewPagerAdapter(this,deckViewModel,cardViewModel)
        TabLayoutMediator(tablayout,viewpager2){tab,position->
            tab.text = tabLayoutTextArray[position]
            tab.setIcon(tabLayoutIconArray[position])
        }.attach()

        deckFab = fab_main
        deckFab.setOnClickListener {
            val dialog = AlertDialog.Builder(this,R.style.DialogStyle)
            val dialogView = layoutInflater.inflate(R.layout.dialog_new_deck,null)
            dialog.setView(dialogView).setPositiveButton("OK") {
                    dialog,i->
                val deck = Deck(0,
                    dialogView.edittext_new_name.text.toString(),
                    dialogView.edittext_new_desc.text.toString(),0)
                deckViewModel.insert(deck)
            }.setNegativeButton("Cancel"){
                    dialog,i->
            }.show()
        }

    }
}
