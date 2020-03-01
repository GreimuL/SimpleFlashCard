package com.greimul.simpleflashcard.adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.greimul.simpleflashcard.Deck
import com.greimul.simpleflashcard.activity.DeckPlayActivity
import com.greimul.simpleflashcard.R
import kotlinx.android.synthetic.main.dialog_deck_click.view.*
import kotlinx.android.synthetic.main.item_deck.view.*

class ViewAdapter(private val viewData:MutableList<Deck>): RecyclerView.Adapter<ViewAdapter.ViewHolder>(){
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
            val dialog = AlertDialog.Builder(parent.context,R.style.DialogStyle)
            val dialogView = LayoutInflater.from(parent.context).inflate(R.layout.dialog_deck_click,parent,false)
            val data = viewData[viewHolder.adapterPosition]

            dialogView.textview_name.text = data.name
            dialogView.textview_desc.text = data.description
            dialogView.textview_size.text = data.cardList.size.toString()

            dialogView.button_delete.setOnClickListener {

            }
            dialogView.button_edit.setOnClickListener {

            }
            dialogView.button_play.setOnClickListener {
                val intent = Intent(parent.context,
                    DeckPlayActivity::class.java)
                parent.context.startActivity(intent.putExtra("size",data.cardList.size))
            }
            dialog.setView(dialogView).show()
        }
        return viewHolder
    }
}