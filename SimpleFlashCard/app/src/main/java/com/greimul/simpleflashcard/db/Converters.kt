package com.greimul.simpleflashcard.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    inline fun <reified T> Gson.fromJson(json:String) = fromJson<T>(json,object: TypeToken<T>() {}.type)

    @TypeConverter
    fun CardlistToJson(cardlist:MutableList<Card>):String{
        return Gson().toJson(cardlist)
    }

    @TypeConverter
    fun JsonToCardlist(json:String):MutableList<Card>{
        val obj = Gson().fromJson<MutableList<Card>>(json)
        return obj
    }

}