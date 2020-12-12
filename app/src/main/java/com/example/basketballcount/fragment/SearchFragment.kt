package com.example.basketballcount.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basketballcount.MainActivity
import com.example.basketballcount.R
import com.example.basketballcount.adaptor.Result
import com.example.basketballcount.adaptor.ResultAdaptor
import com.example.basketballcount.viewmodel.WinGameViewModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchResultList= mutableListOf<Result>()
        search_recyclerview.layoutManager = LinearLayoutManager(requireActivity())
        search_recyclerview.adapter=ResultAdaptor(searchResultList)
search_btn.setOnClickListener{
    var wingame: Long = 0
    var result: String = ""
    var losegame: Long = 0
    val docRef = MainActivity.database.collection("users").document(search_user_et.text.toString())
    docRef.get().addOnSuccessListener { document ->
        result = document.data?.get("result_gson") as String
        losegame = document.data?.get("losegame") as Long
        wingame = document.data?.get("wingame") as Long
        search_win_tv.text=wingame.toString()
        search_lose_tv.text=losegame.toString()
        val makeGson = GsonBuilder().create()
        val listType: TypeToken<MutableList<Result>> = object : TypeToken<MutableList<Result>>() {}
        val datas = makeGson.fromJson<MutableList<Result>>(result, listType.type)
        searchResultList.addAll(datas)
        ResultAdaptor(searchResultList).notifyDataSetChanged()
    }
}


    }
}