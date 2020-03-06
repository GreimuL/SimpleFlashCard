package com.greimul.simpleflashcard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.db.Card
import kotlinx.android.synthetic.main.item_card.view.*

class CardListAdapter: RecyclerView.Adapter<CardListAdapter.ViewHolder>() {

    private var viewData = listOf<Card>()
    private var flip:Boolean = false

    class ViewHolder(v:View):RecyclerView.ViewHolder(v){
        val textView = v.textview_card
        val cardView = v.cardview_card
    }
    override fun getItemCount(): Int = viewData.size

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.itemView.startAnimation(AlphaAnimation(0f,1f).apply{
            duration = 300
        })
        if(flip)
            holder.textView.text = viewData[position].back
        else
            holder.textView.text = viewData[position].front
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_card,parent,false)
        val viewHolder = ViewHolder(v)

        viewHolder.cardView.setOnClickListener {

        }

        return viewHolder
    }

    fun setCards(cardList:List<Card>){
        viewData = cardList
        notifyDataSetChanged()
    }

    fun flipCards(){
        flip = !flip
        notifyDataSetChanged()
    }

}