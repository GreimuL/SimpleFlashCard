package com.greimul.simpleflashcard.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.adapter.ExtractListAdapter
import com.greimul.simpleflashcard.db.Card
import com.greimul.simpleflashcard.db.Deck
import com.greimul.simpleflashcard.db.DeckDatabase
import com.greimul.simpleflashcard.viewmodel.CardViewModel
import com.greimul.simpleflashcard.viewmodel.DeckViewModel
import kotlinx.android.synthetic.main.dialog_new_deck.view.*
import kotlinx.android.synthetic.main.fragment_im_export.*
import kotlinx.android.synthetic.main.fragment_im_export.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.properties.Delegates

class ImExportFragment(val deckViewModel:DeckViewModel,val cardViewModel: CardViewModel) : Fragment(){

    lateinit var extractListAdapter:ExtractListAdapter
    lateinit var testList:List<Card>

    var deckList:List<Deck> = listOf()

    val cardList = mutableListOf<Card>()
    var cardListFromDeck = listOf<Card>()

    var isTypeSeleted:Boolean = false
    var chooseType:Int = 0

    var selectedDeckId:Int = -1

    var isDeckSelected:Boolean = false
    var isFileSelected:Boolean = false

    var delimiterForWrite:String = " "

    lateinit var deckUri:Uri
    lateinit var writeUri:Uri

    var isWriteUriSet:Boolean = false

    var currentSelectedEncoding:String = "UTF8"

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==3&&resultCode== Activity.RESULT_OK){
            data?.data?.also {
                deckUri = it
                isFileSelected = true
            }
        }
        else if(requestCode==4&&resultCode==Activity.RESULT_OK){
            data?.data?.also{
                writeUri = it
                isWriteUriSet = true
                writeFile()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_im_export,container,false)

        deckViewModel.deckList.observe(this,
            Observer {
                deckList = it
            })

        extractListAdapter = ExtractListAdapter()
        view.recyclerview_import_text.apply{
            setHasFixedSize(true)
            adapter = extractListAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }
        view.button_choose_file_deck.setOnClickListener {
            if(isTypeSeleted) {
                if(chooseType==0){
                    openCardListFile()
                }
                else if(chooseType==1){
                    openDeckList()
                }
            }
            else {
                val dialogBuilder =
                    AlertDialog.Builder(context, R.style.DialogStyle).setItems(R.array.choose,
                        DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                0 -> {
                                    isTypeSeleted = true
                                    chooseType = 0
                                    openCardListFile()
                                    view.button_import.text = "IMPORT"
                                }
                                1 -> {
                                    isTypeSeleted = true
                                    chooseType = 1
                                    openDeckList()
                                    view.button_import.text = "EXPORT"
                                }
                            }
                        }).show()
            }
        }
        view.button_extract.setOnClickListener {
            val str:String
            val cardListSet:List<Card>
            val delimiter:Char

            if(edittext_delimiter.text.isNotEmpty())
                delimiter = edittext_delimiter.text.toString()[0]
            else
                delimiter = '\n'

            if(isFileSelected&&chooseType==0) {
                str = getStringFromUri(deckUri)
                cardListSet = getCardListFromString(str,delimiter)
                extractListAdapter.setExtractList(cardListSet)
            }
            else if(isDeckSelected){
                cardListFromDeck.forEach{
                    cardList.add(it)
                }
                cardListSet = cardList
                extractListAdapter.setExtractList(cardListSet)
            }
            else{
                Toast.makeText(context,"Select File First",Toast.LENGTH_SHORT).show()
            }
        }
        view.button_clear.setOnClickListener {
            cardList.clear()
            extractListAdapter.setExtractList(cardList)
            isTypeSeleted = false
            isDeckSelected = false
            isFileSelected = false
            isWriteUriSet = false
            view.button_import.text = "File > Import, Deck > Export"
        }
        view.button_import.setOnClickListener {
            if(chooseType==0) {
                val dialog = AlertDialog.Builder(context, R.style.DialogStyle)
                val dialogView = layoutInflater.inflate(R.layout.dialog_new_deck, null)
                dialog.setView(dialogView).setPositiveButton("OK") { dialog, i ->
                    val deck = Deck(
                        0,
                        dialogView.edittext_new_name.text.toString(),
                        dialogView.edittext_new_desc.text.toString(), 0
                    )
                    var deckId: Long = 0
                    deckViewModel.insert(deck)
                    deckViewModel.recentInsertedDeckId.observe(this@ImExportFragment, Observer {
                        deckId = it
                        cardList.forEach {
                            cardViewModel.insert(Card(0, it.front, it.back, deckId.toInt()))
                        }
                    })
                }.setNegativeButton("Cancel") { dialog, i ->
                }.show()
            }
            else if(chooseType==1){
                if(edittext_delimiter.text.isNotEmpty())
                    delimiterForWrite = edittext_delimiter.text.toString()
                else
                    delimiterForWrite = "\n"
                createDeckFile()
            }
        }

        ArrayAdapter.createFromResource(view.context,R.array.encoding_array,android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.spinner_encoding.adapter = it
        }

        view.spinner_encoding.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position==0)
                    currentSelectedEncoding = "UTF8"
                else
                    currentSelectedEncoding = "MS949"
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return view
    }

    fun openDeckList(){
        val deckNameList = mutableListOf<String>()
        deckList.forEach {
            deckNameList.add(it.name)
        }
        val dialogBuilder =
            AlertDialog.Builder(context,R.style.DialogStyle).setItems(deckNameList.toTypedArray(),
                DialogInterface.OnClickListener{ dialog, which ->
                    selectedDeckId = deckList[which].id
                    isDeckSelected = true
                    cardViewModel.getCardFromDeck(selectedDeckId).observe(this, Observer {
                        cardListFromDeck = it
                    })
                }).show()
    }

    fun writeFile(){
        context?.contentResolver?.openFileDescriptor(writeUri,"w")?.use{
            FileOutputStream(it.fileDescriptor).use{file->
                cardList.forEach {
                    file.write(it.front.toByteArray(StandardCharsets.UTF_8))
                    file.write(delimiterForWrite.toByteArray(StandardCharsets.UTF_8))
                    file.write(it.back.toByteArray(StandardCharsets.UTF_8))
                    file.write(delimiterForWrite.toByteArray(StandardCharsets.UTF_8))
                }
            }
        }
    }

    fun createDeckFile(){
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply{
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        startActivityForResult(intent,4)
    }

    fun openCardListFile(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        startActivityForResult(intent,3)
    }

    fun getStringFromUri(uri:Uri):String{
        val strBuilder = StringBuilder()
        context?.contentResolver?.openInputStream(uri).use{
            BufferedReader(InputStreamReader(it,currentSelectedEncoding)).use{ bufferedReader->
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