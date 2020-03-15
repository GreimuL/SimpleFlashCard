package com.greimul.simpleflashcard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.db.Card
import kotlinx.android.synthetic.main.item_extract_text.view.*

class ExtractListAdapter: RecyclerView.Adapter<ExtractListAdapter.ViewHolder>() {

    var viewData:List<Card> = listOf()

    class ViewHolder(v: View):RecyclerView.ViewHolder(v){
        val extractFront = v.textview_extract_front
        val extractBack = v.textview_extract_back
    }

    override fun getItemCount(): Int = viewData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.extractFront.text = viewData[position].front
        holder.extractBack.text = viewData[position].back
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_extract_text,parent,false)
        return ViewHolder(v)
    }

    fun setExtractList(extractList:List<Card>){
        viewData = extractList
        notifyDataSetChanged()
    }
}