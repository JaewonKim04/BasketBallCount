package com.example.basketballcount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WingameViewModel(userId: Int) :ViewModel() {
    private val liveWin=MutableLiveData<String>()
    fun getText(): LiveData<String>{
        return liveWin
    }
    fun setText(text:String){
        liveWin.value=text
    }
}