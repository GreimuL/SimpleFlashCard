package com.greimul.simpleflashcard.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.ExtractListAdapter
import com.greimul.simpleflashcard.db.Card
import com.greimul.simpleflashcard.db.Deck
import com.greimul.simpleflashcard.viewmodel.DeckViewModel
import kotlinx.android.synthetic.main.dialog_new_deck.view.*
import kotlinx.android.synthetic.main.fragment_im_export.*
import kotlinx.android.synthetic.main.fragment_im_export.view.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI

class ImExportFragment(val deckViewModel:DeckViewModel) : Fragment(){

    lateinit var extractListAdapter:ExtractListAdapter
    lateinit var testList:List<Card>

    val cardList = mutableListOf<Card>()

    var isFileSelected:Boolean = false
    lateinit var deckUri:Uri

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==3&&resultCode== Activity.RESULT_OK){
            data?.data?.also {
                deckUri = it
                isFileSelected = true
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_im_export,container,false)
        extractListAdapter = ExtractListAdapter()
        view.recyclerview_import_text.apply{
            setHasFixedSize(true)
            adapter = extractListAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }
        view.button_choose_file.setOnClickListener {
            openCardListFile()
        }
        view.button_extract.setOnClickListener {
            val str:String
            val cardList:List<Card>
            val delimiter:Char

            if(edittext_delimiter.text.isNotEmpty())
                delimiter = edittext_delimiter.text.toString()[0]
            else
                delimiter = '\n'

            if(isFileSelected) {
                str = getStringFromUri(deckUri)
                cardList = getCardListFromString(str,delimiter)
                extractListAdapter.setExtractList(cardList)
            }
            else{

            }
        }
        view.button_clear.setOnClickListener {
            cardList.clear()
            extractListAdapter.setExtractList(cardList)
        }
        view.button_import.setOnClickListener {
            val dialog = AlertDialog.Builder(context,R.style.DialogStyle)
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
        return view
    }

    fun openCardListFile(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        startActivityForResult(intent,3)
    }
    //Charset to CP949... is this really best solution?
    fun getStringFromUri(uri:Uri):String{
        val strBuilder = StringBuilder()
        context?.contentResolver?.openInputStream(uri).use{
            BufferedReader(InputStreamReader(it,"CP949")).use{ bufferedReader->
                var currentLine = bufferedReader.readLine()
                while(currentLine!=null){
                    strBuilder.append(currentLine)
                    strBuilder.append('\n')
                    Log.d("test",currentLine)
                    currentLine = bufferedReader.readLine()
                }
                if(strBuilder.isNotEmpty())
                    strBuilder.deleteCharAt(strBuilder.length-1)
            }
        }
        return strBuilder.toString()
    }

    fun getCardListFromString(str:String, delimiter:Char):List<Card>{
        var isFront:Boolean = true
        var front:String? = null
        var back:String? = null

        var currentWord = StringBuilder()
        str.forEach{
            if(it==delimiter) {
                if(isFront) {
                    isFront = !isFront
                    front = currentWord.toString()
                }
                else {
                    isFront = !isFront
                    back = currentWord.toString()
                    cardList.add(Card(0,front?:"",back?:"",0))
                    front = null
                    back = null
                }
                currentWord.clear()
            }
            else
                currentWord.append(it)
        }
        if(currentWord.isNotEmpty())
            if(front!=null)
                back = currentWord.toString()
            else
                front = currentWord.toString()

        cardList.add(Card(0,front?:"",back?:"",0))


        return cardList
    }
}