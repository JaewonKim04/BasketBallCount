package com.example.basketballcount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WinGameViewModel :ViewModel(){
    private val _updateWinGame=MutableLiveData<String>()
    val updateWinGame:LiveData<String>get()=_updateWinGame
    fun changeWingGame(){
        _updateWinGame.value=updateWinGame.value
    }
}