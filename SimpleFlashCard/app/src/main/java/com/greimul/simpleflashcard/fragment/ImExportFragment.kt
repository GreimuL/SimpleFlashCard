package com.greimul.simpleflashcard.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.net.toFile
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
import kotlinx.android.synthetic.main.dialog_progress.view.*
import kotlinx.android.synthetic.main.fragment_im_export.*
import kotlinx.android.synthetic.main.fragment_im_export.view.*
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.properties.Delegates

class ImExportFragment(val deckViewModel:DeckViewModel,val cardViewModel: CardViewModel) : Fragment(){

    lateinit var extractListAdapter:ExtractListAdapter
    lateinit var testList:List<Card>

    lateinit var fragmentView: View

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
                showFileName(it)
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
        fragmentView = inflater.inflate(R.layout.fragment_im_export,container,false)

        deckViewModel.deckList.observe(this,
            Observer {
                deckList = it
            })

        extractListAdapter = ExtractListAdapter()
        fragmentView.recyclerview_import_text.apply{
            setHasFixedSize(true)
            adapter = extractListAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }
        fragmentView.button_choose_file_deck.setOnClickListener {
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
                                    fragmentView.button_import.text = "IMPORT"
                                    button_choose_file_deck.text = "File Select Mode"
                                }
                                1 -> {
                                    isTypeSeleted = true
                                    chooseType = 1
                                    openDeckList()
                                    fragmentView.button_import.text = "EXPORT"
                                    button_choose_file_deck.text = "Deck Select Mode"
                                }
                            }
                        }).show()
            }
        }
        fragmentView.button_extract.setOnClickListener {
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
                Toast.makeText(context,"Select File or Deck First",Toast.LENGTH_SHORT).show()
            }
        }
        fragmentView.button_clear.setOnClickListener {
            clearAll()
        }
        fragmentView.button_import.setOnClickListener {
            if(isTypeSeleted==true) {
                if (chooseType == 0) {
                    val dialog = AlertDialog.Builder(context, R.style.DialogStyle)
                    val dialogView = layoutInflater.inflate(R.layout.dialog_new_deck, null)
                    dialog.setView(dialogView).setPositiveButton("OK") { dialog, i ->
                        val deck = Deck(
                            0,
                            dialogView.edittext_new_name.text.toString(),
                            dialogView.edittext_new_desc.text.toString(), 0
                        )
                        var deckId: Long = 0
                        val job = Job()
                        var finish = true
                        var curProgress = 0
                        deckViewModel.insert(deck).invokeOnCompletion {
                            deckId = deckViewModel.recentInsertedDeckId
                            cardList.forEach {
                                cardViewModel.insert(Card(0, it.front, it.back, deckId.toInt()))
                            }
                            deckViewModel.countCards(deckId.toInt()).observe(this@ImExportFragment,
                                object:Observer<Int> {
                                    override fun onChanged(t: Int?) {
                                        if(t!=null){
                                            if(t==cardList.size){
                                                finish = false
                                                deckViewModel.countCards(deckId.toInt()).removeObserver(this)
                                            }
                                            curProgress=t
                                        }

                                    }
                                })
                            val dialogBuilder = AlertDialog.Builder(context)
                            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_progress,container,false)
                            dialogBuilder.setCancelable(false)
                            dialogBuilder.setView(dialogView)
                            val dialog = dialogBuilder.create()
                            dialog.show()
                            fragmentView.button_import.isClickable = false
                            GlobalScope.launch(Dispatchers.IO) {
                                while (finish) {
                                    withContext(Dispatchers.Main) {
                                        dialogView.textview_progress.text =
                                            "wait... $curProgress / ${cardList.size}"
                                    }
                                    delay(10L)
                                }
                                dialog.dismiss()
                                withContext(Dispatchers.Main){
                                    fragmentView.button_import.isClickable = true
                                    Toast.makeText(context,"Import Success!",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        /*
                        deckViewModel.recentInsertedDeckId.observe(this@ImExportFragment, object:Observer<Long> {
                                override fun onChanged(t: Long?) {
                                    if(t!=null)
                                        deckId = t
                                    cardList.forEach {
                                        cardViewModel.insert(Card(0, it.front, it.back, deckId.toInt()))
                                    }

                                    deckViewModel.countCards(deckId.toInt()).observe(this@ImExportFragment,
                                        object:Observer<Int> {
                                            override fun onChanged(t: Int?) {
                                                if(t!=null){
                                                    if(t==cardList.size){
                                                        finish = false
                                                        deckViewModel.countCards(deckId.toInt()).removeObserver(this)
                                                    }
                                                    curProgress=t
                                                }

                                            }
                                        })
                                    deckViewModel.recentInsertedDeckId.removeObserver(this)
                                }
                            })
                            GlobalScope.launch(Dispatchers.IO) {
                                withContext(Dispatchers.Main) {
                                    button_import.text = "wait"
                                    button_import.isClickable = false
                                }
                                while(finish) {
                                    withContext(Dispatchers.Main) {
                                        button_import.text =
                                            "wait $curProgress / ${cardList.size}"
                                    }
                                    delay(10L)
                                }
                                withContext(Dispatchers.Main) {
                                    clearAll()
                                    button_import.text = "success"
                                    button_import.isClickable = true
                                }

                        }

                         */
                    }.setNegativeButton("Cancel") { dialog, i ->
                    }.show()
                } else if (chooseType == 1) {
                    if (edittext_delimiter.text.isNotEmpty())
                        delimiterForWrite = edittext_delimiter.text.toString()
                    else
                        delimiterForWrite = "\n"
                    createDeckFile()
                }
            }
            else{
                Toast.makeText(context,"Select File or Deck First",Toast.LENGTH_SHORT).show()
            }
        }

        ArrayAdapter.createFromResource(fragmentView.context,R.array.encoding_array,android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fragmentView.spinner_encoding.adapter = it
        }

        fragmentView.spinner_encoding.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
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

        return fragmentView
    }

    fun clearAll(){
        cardList.clear()
        extractListAdapter.setExtractList(cardList)
        isTypeSeleted = false
        isDeckSelected = false
        isFileSelected = false
        isWriteUriSet = false
        fragmentView.button_choose_file_deck.text = "Choose File or Deck..."
        fragmentView.button_import.text = "File > Import\nDeck > Export"
    }

    fun showFileName(uri:Uri){
        val cursor = context?.contentResolver?.query(uri,null,null,null)
        cursor?.use{
            if(it.moveToFirst()){
                val fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                fragmentView.button_choose_file_deck.text = fileName
            }
        }
    }

    fun openDeckList(){
        val deckNameList = mutableListOf<String>()
        deckList.forEach {
            deckNameList.add(it.name)
        }
        if(deckNameList.isEmpty()){
            Toast.makeText(context,"Deck List is Empty!",Toast.LENGTH_SHORT).show()
            return
        }
        val dialogBuilder =
            AlertDialog.Builder(context,R.style.DialogStyle).setItems(deckNameList.toTypedArray()
            ) { dialog, which ->
                fragmentView.button_choose_file_deck.text = deckList[which].name
                selectedDeckId = deckList[which].id
                isDeckSelected = true
                cardViewModel.getCardFromDeck(selectedDeckId).observe(this, Observer {
                    cardListFromDeck = it
                })
            }.show()
    }

    fun writeFile(){
        context?.contentResolver?.openFileDescriptor(writeUri,"w")?.use{
            FileOutputStream(it.fileDescriptor).use{file->
                cardList.forEach {
                    file.write(it.front.toByteArray(Charset.forName(currentSelectedEncoding)))
                    file.write(delimiterForWrite.toByteArray(Charset.forName(currentSelectedEncoding)))
                    file.write(it.back.toByteArray(Charset.forName(currentSelectedEncoding)))
                    file.write(delimiterForWrite.toByteArray(Charset.forName(currentSelectedEncoding)))
                }
            }
        }
        Toast.makeText(context,"Export Success!",Toast.LENGTH_SHORT).show()
        val dialogBuilder = AlertDialog.Builder(context,R.style.DialogStyle)
        val dialog = dialogBuilder.setView(R.layout.dialog_share).setPositiveButton("OK"){
            dialog,which->
            val shareIntent = Intent().apply{
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM,writeUri)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent,"Choose File"))
        }.setNegativeButton("Cancel"){
            dialog, which ->
        }.show()
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