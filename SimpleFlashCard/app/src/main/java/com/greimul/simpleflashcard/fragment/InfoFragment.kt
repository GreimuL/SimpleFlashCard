package com.greimul.simpleflashcard.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.greimul.simpleflashcard.R
import kotlinx.android.synthetic.main.fragment_info.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InfoFragment : Fragment() {
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
        GlobalScope.launch(Dispatchers.Main) {
            while(true) {
                currentDate = Date()
                milliSecond = targetDate.time - currentDate.time
                if(milliSecond<=0){
                    view.textview_rml.text = ""
                    view.textview_d_day.text = ""
                    break
                }
                view.textview_d_day.text = ((milliSecond)/(1000*24*60*60)).toString()+":"+
                        ((milliSecond)%(1000*24*60*60)/(1000*60*60))+":"+
                        ((milliSecond)%(1000*24*60*60)%(1000*60*60)/(1000*60))+":"+
                        ((milliSecond)%(1000*24*60*60)%(1000*60*60)%(1000*60)/(1000))
                delay(1000L)
            }
        }.start()

        return view
    }
}