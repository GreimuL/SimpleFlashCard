package com.greimul.simpleflashcard.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.greimul.simpleflashcard.R
import com.greimul.simpleflashcard.viewmodel.DeckViewModel
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.dialog_progress.view.*
import kotlinx.android.synthetic.main.fragment_info.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadPoolExecutor

class InfoFragment(val deckViewModel:DeckViewModel) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val view = inflater.inflate(R.layout.fragment_info,container,false)

        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val targetDate = format.parse("2020-10-23 00:00:00")
        var currentDate = Date()
        var milliSecond:Long
        var textString:String
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                while (true) {
                    currentDate = Date()
                    milliSecond = targetDate.time - currentDate.time
                    if (milliSecond <= 0) {
                        view.textview_rml.text = ""
                        view.textview_d_day.text = ""
                        break
                    }
                    view.textview_d_day.text =
                        ((milliSecond) / (1000 * 24 * 60 * 60)).toString() + ":" +
                                ((milliSecond) % (1000 * 24 * 60 * 60) / (1000 * 60 * 60)) + ":" +
                                ((milliSecond) % (1000 * 24 * 60 * 60) % (1000 * 60 * 60) / (1000 * 60)) + ":" +
                                ((milliSecond) % (1000 * 24 * 60 * 60) % (1000 * 60 * 60) % (1000 * 60) / (1000))
                    delay(1000L)
                }
            }
        }.start()

        view.button_reset.setOnClickListener{
            val firstDialog  = AlertDialog.Builder(context,R.style.DialogStyle)
            val firstDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_all,container,false)

            firstDialog.setView(firstDialogView).setPositiveButton("OK"){
                first, _ ->
                first.dismiss()
                val dialogBuilder = AlertDialog.Builder(context)
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_progress,container,false)
                dialogBuilder.setCancelable(false)
                dialogBuilder.setView(dialogView)
                dialogView.textview_progress.text = "wait..."
                val dialog = dialogBuilder.create()
                dialog.show()
                deckViewModel.deleteAllDeck().invokeOnCompletion {
                    dialog.dismiss()
                }
                val intent = activity?.intent
                activity?.finish()
                startActivity(intent)
            }.setNegativeButton("Cancel"){
                dialog, which ->
            }.show()

        }
        return view
    }
}