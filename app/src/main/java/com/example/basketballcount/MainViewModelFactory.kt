package com.example.basketballcount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory<T>(var userId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WingameViewModel::class.java)) {
            return WingameViewModel(userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}