package com.example.basketballcount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WinGameViewModel : ViewModel() {
    val wingame = MutableLiveData<String>()
    val losegame=MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    fun setWinGame(text: String) {
        wingame.value = text
    }

    fun setUserName(text: String) {
        userName.value = text
    }
    fun setLoseGame(text:String){
        losegame.value=text
    }

}