package com.greimul.simpleflashcard.adapter

import android.animation.AnimatorInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.db.Card
import kotlinx.android.synthetic.main.item_card.view.*
import kotlinx.android.synthetic.main.item_card_play.view.*
import java.util.*

class CardAdapter(private val seekBar: SeekBar?, private val type:Int): RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    var originData:List<Card> = listOf()
    var viewData:List<Card> = listOf()
    var flipSet: BitSet = BitSet()
    var isAllFlip:Boolean = false

    class ViewHolder(v: View, type:Int): RecyclerView.ViewHolder(v){
        lateinit var textView: TextView
        lateinit var cardView: CardView
        init{
            when(type){
                0-> {
                    textView = v.textview_card_text
                    cardView = v.cardview_card
                }
                1->{
                    textView = v.textview_play_card_text
                    cardView = v.cardview_play_card
                }
            }
        }
    }

    override fun getItemCount(): Int = viewData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        flipAnimation(holder.cardView)
        if(type==0)
            holder.itemView.startAnimation(AlphaAnimation(0f,1f).apply{
                duration=300
            })
        if(flipSet[position])
            holder.textView.text = viewData[holder.adapterPosition].back
        else
            holder.textView.text = viewData[holder.adapterPosition].front
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = when(type){
            0-> LayoutInflater.from(parent.context).inflate(R.layout.item_card,parent,false)
            1-> LayoutInflater.from(parent.context).inflate(R.layout.item_card_play,parent,false)
            else-> LayoutInflater.from(parent.context).inflate(R.layout.item_card_play,parent,false)
        }
        val viewHolder = ViewHolder(v,type)
        val curView = viewHolder.cardView
        val curText = viewHolder.textView
        curView.setOnClickListener {
            flipAnimation(curView)
            flipSet[viewHolder.adapterPosition] = !flipSet[viewHolder.adapterPosition]
            if(flipSet[viewHolder.adapterPosition]) {
                curText.text = viewData[viewHolder.adapterPosition].back
            }
            else {
                curText.text = viewData[viewHolder.adapterPosition].front
            }
        }

        return viewHolder
    }

    fun setCards(cardList:List<Card>){
        viewData = cardList
        originData = cardList
        flipSet = BitSet(viewData.size)
        notifyDataSetChanged()
        if(seekBar!=null)
            seekBar.max = viewData.size-1
    }

    fun flipAnimation(cardView: CardView){
        cardView.startAnimation(AlphaAnimation(1f,0f).apply{duration=500})
        cardView.startAnimation(AlphaAnimation(0f,1f).apply{duration=500})
    }

    fun flipCard(position: Int){
        flipSet.flip(position)
        notifyItemChanged(position)
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