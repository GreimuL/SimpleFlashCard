package com.greimul.simpleflashcard.adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.greimul.simpleflashcard.db.Deck
import com.greimul.simpleflashcard.activity.DeckPlayActivity
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.activity.CardListActivity
import com.greimul.simpleflashcard.viewmodel.DeckViewModel
import kotlinx.android.synthetic.main.dialog_deck_click.view.*
import kotlinx.android.synthetic.main.item_deck.view.*
import java.lang.Exception

class DeckListAdapter(val activity:FragmentActivity?,val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<DeckListAdapter.ViewHolder>(){

    private var viewData = listOf<Deck>()
    private var viewModel:DeckViewModel

    init{
        viewModel = activity?.run{
            ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application))
                .get(DeckViewModel::class.java)
        }?:throw Exception("invalid")
    }

    class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        val button:Button = v.button_deck
    }

    override fun getItemCount(): Int = viewData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.button.text = viewData[position].name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_deck,parent,false)
        val viewHolder = ViewHolder(v)
        viewHolder.button.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(parent.context,R.style.DialogStyle)
            val dialogView = LayoutInflater.from(parent.context).inflate(R.layout.dialog_deck_click,parent,false)
            val data = viewData[viewHolder.adapterPosition]

            dialogView.textview_name.text = data.name
            dialogView.textview_desc.text = data.description
            viewModel.countCards(data.id).observe(lifecycleOwner, Observer {
                dialogView.textview_size.text = it.toString()
            })

            val dialog = dialogBuilder.setView(dialogView).create()

            dialogView.button_delete.setOnClickListener {
                viewModel.delete(data.id)
                dialog.dismiss()
            }
            dialogView.button_edit.setOnClickListener {
                val intent = Intent(parent.context,
                    CardListActivity::class.java)
                parent.context.startActivity(intent.putExtra("deckId",data.id))
            }
            dialogView.button_play.setOnClickListener {
                val intent = Intent(parent.context,
                    DeckPlayActivity::class.java)
                parent.context.startActivity(intent.putExtra("deckId",data.id))
            }
            dialog.show()
        }
        return viewHolder
    }

    fun setDeck(deckList:List<Deck>){
        viewData = deckList
        notifyDataSetChanged()
    }
}