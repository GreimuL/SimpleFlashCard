package com.greimul.simpleflashcard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.db.Card

class CardListAdapter: RecyclerView.Adapter<CardListAdapter.ViewHolder>() {

    private var viewData = listOf<Card>()

    class ViewHolder(v:View):RecyclerView.ViewHolder(v){
    }
    override fun getItemCount(): Int = viewData.size

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_card,parent,false)
        val viewHolder = ViewHolder(v)



        return viewHolder
    }

    fun setCards(cardList:List<Card>){
        viewData = cardList
        notifyDataSetChanged()
    }

}