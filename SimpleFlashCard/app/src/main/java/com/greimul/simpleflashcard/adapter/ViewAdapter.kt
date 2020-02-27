package com.greimul.simpleflashcard.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.greimul.simpleflashcard.Deck
import com.greimul.simpleflashcard.R
import kotlinx.android.synthetic.main.item_deck.view.*

class ViewAdapter(private val viewData:MutableList<Deck>): RecyclerView.Adapter<ViewAdapter.ViewHolder>(){
    class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        val button:Button
        init{
            v.setOnClickListener{ println("asdf") }
            button = v.button
        }
    }

    override fun getItemCount(): Int = viewData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.button.text = viewData[position].name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_deck,parent,false)
        return ViewHolder(v)
    }
}