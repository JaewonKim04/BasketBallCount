package com.example.basketballcount.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basketballcount.adaptor.Result

class WinGameViewModel : ViewModel() {
    val wingame = MutableLiveData<String>()
    val losegame=MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val userEmail=MutableLiveData<String>()
    val resultRecyclerView= MutableLiveData<MutableList<Result>>()
    fun setWinGame(text: String) {
        wingame.value = text
    }

    fun setUserName(text: String) {
        userName.value = text
    }
    fun setLoseGame(text:String){
        losegame.value=text
    }

    fun setResult(result:MutableList<Result>){
        resultRecyclerView.value=result
    }
    fun setEmail(text:String){
        userEmail.value=text
    }

}