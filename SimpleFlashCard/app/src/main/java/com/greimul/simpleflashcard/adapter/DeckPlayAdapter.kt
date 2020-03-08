package com.greimul.simpleflashcard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.db.Card
import kotlinx.android.synthetic.main.item_card_play.view.*
import java.util.*

class DeckPlayAdapter(seekBar: SeekBar):RecyclerView.Adapter<DeckPlayAdapter.ViewHolder>() {

    var originData:List<Card> = listOf()
    var viewData:List<Card> = listOf()
    val seekBar = seekBar
    var flipSet:BitSet = BitSet()
    var isAllFlip:Boolean = false

    class ViewHolder(v: View):RecyclerView.ViewHolder(v){
        val textView: TextView = v.textview_play_card_text
        val cardView: CardView = v.cardview_play_card
    }

    override fun getItemCount(): Int = viewData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(flipSet[position])
            holder.textView.text = "${viewData[holder.adapterPosition].id}"+viewData[holder.adapterPosition].back
        else
            holder.textView.text = "${viewData[holder.adapterPosition].id}"+viewData[holder.adapterPosition].front
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_card_play,parent,false)
        val viewHolder = ViewHolder(v)

        viewHolder.cardView.setOnClickListener {
            flipSet[viewHolder.adapterPosition] = !flipSet[viewHolder.adapterPosition]
            if(flipSet[viewHolder.adapterPosition])
                viewHolder.textView.text = "${viewData[viewHolder.adapterPosition].id}"+viewData[viewHolder.adapterPosition].back
            else
                viewHolder.textView.text = "${viewData[viewHolder.adapterPosition].id}"+viewData[viewHolder.adapterPosition].front
        }

        return viewHolder
    }

    fun setCards(cardList:List<Card>){
        viewData = cardList
        originData = cardList
        flipSet = BitSet(viewData.size)
        notifyDataSetChanged()
        seekBar.max = viewData.size-1
    }

    fun flipCard(position: Int){
        flipSet.flip(position)
        notifyDataSetChanged()
    }

    fun flipAllCards(){
        isAllFlip = !isAllFlip
        if(isAllFlip)
            flipSet.set(0,flipSet.size()-1,true)
        else
            flipSet.set(0,flipSet.size()-1,false)
        notifyDataSetChanged()
    }

    fun shuffleCards(){
        val dataArray = mutableListOf<Card>()
        viewData.forEach{
            dataArray.add(it)
        }
        var piv = 1
        for(i in 0 until viewData.size-1){
            var rand = (piv until viewData.size).random()
            dataArray[i] = dataArray[rand].also { dataArray[rand] = dataArray[i] }
            piv++
        }
        viewData = dataArray.toList()
        notifyDataSetChanged()
    }
}