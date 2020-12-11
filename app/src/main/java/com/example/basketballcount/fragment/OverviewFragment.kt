package com.example.basketballcount.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basketballcount.MainActivity
import com.example.basketballcount.MainActivity.Companion.SHARED_EMAIL
import com.example.basketballcount.MainActivity.Companion.SHARED_LOSE
import com.example.basketballcount.login.GetUserNameActivity
import com.example.basketballcount.MainActivity.Companion.SHARED_RESULT
import com.example.basketballcount.MainActivity.Companion.SHARED_WIN
import com.example.basketballcount.MainActivity.Companion.editor
import com.example.basketballcount.MainActivity.Companion.listType
import com.example.basketballcount.MainActivity.Companion.makeGson
import com.example.basketballcount.MainActivity.Companion.overviewList
import com.example.basketballcount.MainActivity.Companion.result
import com.example.basketballcount.MainActivity.Companion.startShared
import com.example.basketballcount.MainActivity.Companion.model
import com.example.basketballcount.R
import com.example.basketballcount.viewmodel.WinGameViewModel
import com.example.basketballcount.adaptor.Result
import com.example.basketballcount.adaptor.ResultAdaptor
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_overview.view.*


class OverviewFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        view.logout_btn.setOnClickListener {
            activity?.let {
                editor.clear()
                editor.apply()
                val intent = Intent(context, GetUserNameActivity::class.java)
                startActivityForResult(intent, 1)
            }

        }
        view.email_tv.text= startShared.getString(SHARED_EMAIL,"").toString()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email_tv.text= startShared.getString(SHARED_EMAIL,"").toString()
        result = ResultAdaptor(overviewList)
        val makeGson = GsonBuilder().create()
        val listType: TypeToken<MutableList<Result>> = object : TypeToken<MutableList<Result>>() {}
        val model = ViewModelProvider(requireActivity()).get(WinGameViewModel::class.java)
        val strContact = makeGson.toJson(overviewList, listType.type)
        editor.putString(SHARED_RESULT, strContact)
        editor.apply()
        overviewList.clear()
        val email= startShared.getString(SHARED_EMAIL,"").toString()
        val wingame= startShared.getInt(SHARED_WIN,0)
        val losegame= startShared.getInt(SHARED_LOSE,0)
        val getRecycler = startShared.getString(SHARED_RESULT, "").toString()
        val datas = makeGson.fromJson<MutableList<Result>>(getRecycler, listType.type)
        overviewList.addAll(datas)
        result.notifyDataSetChanged()
        result_recyclerview.adapter = result
        result_recyclerview.layoutManager = LinearLayoutManager(requireActivity())
        model.wingame.observe(viewLifecycleOwner, Observer {
            win_tv.text = it
        })
        model.userName.observe(viewLifecycleOwner, Observer {
            name_tv.text = it
        })
        model.losegame.observe(viewLifecycleOwner, Observer {
            lose_tv.text = it
        })
        model.resultRecyclerView.observe(viewLifecycleOwner, Observer {
            editor.putString(SHARED_RESULT, strContact)
            editor.commit()
            result.notifyDataSetChanged()
            if (email!="") {
                addToFirebase(email,getRecycler,wingame,losegame)
        }
        })
    }

    private fun addToFirebase(email:String,result:String,wingame:Int,loseGame:Int){
        val setwin=wingame.toLong()
        val setlose=loseGame.toLong()
        val user= hashMapOf(
            "result_gson" to result,
            "losegame" to setlose,
            "wingame" to setwin
        )
        MainActivity.database.collection("users").document(email).set(user).addOnSuccessListener {        }
    }

}