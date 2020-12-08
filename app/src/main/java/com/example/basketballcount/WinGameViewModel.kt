package com.example.basketballcount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basketballcount.adaptor.Result

class WinGameViewModel : ViewModel() {
    val wingame = MutableLiveData<String>()
    val losegame=MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val resultRecyclerView= MutableLiveData<Result>()
    fun setWinGame(text: String) {
        wingame.value = text
    }

    fun setUserName(text: String) {
        userName.value = text
    }
    fun setLoseGame(text:String){
        losegame.value=text
    }

    fun setResult(result:Result){
        resultRecyclerView.value=result
    }

}