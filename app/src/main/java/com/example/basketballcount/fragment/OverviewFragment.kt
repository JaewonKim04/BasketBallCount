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
        result = ResultAdaptor(overviewList)//
        val makeGson = GsonBuilder().create()
        val listType: TypeToken<MutableList<Result>> = object : TypeToken<MutableList<Result>>() {}
        val model = ViewModelProvider(requireActivity()).get(WinGameViewModel::class.java)
        val strContact = makeGson.toJson(overviewList, listType.type)
        editor.putString(SHARED_RESULT, strContact)
        editor.apply()
        overviewList.clear()
        Log.d("itemCount", "리셋됨")
        val email= startShared.getString(SHARED_EMAIL,"").toString()
        val getRecycler = startShared.getString(SHARED_RESULT, "")
        val wingame= startShared.getInt(SHARED_WIN,0)
        val losegame= startShared.getInt(SHARED_LOSE,0)
        if (email!="") {
            if (getRecycler != null) {
                addToFirebase(email,getRecycler,wingame,losegame)
            }
        }
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
            Log.d("itemCount", result.itemCount.toString())
            result.notifyDataSetChanged()
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
        MainActivity.database.collection("users").document(email).set(user).addOnSuccessListener {

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            var result = ""
            var email = ""
            var wingame = 0
            var losegame = 0
            val userName = data!!.getStringExtra("get_name").toString()
            email = data.getStringExtra("get_email").toString()
            Log.d("이메일", startShared.getString(SHARED_EMAIL, "안되네").toString())
            wingame = data.getLongExtra("get_win_fire", 0).toInt()
            losegame = data.getLongExtra("get_lose_fire", 0).toInt()
            result = data.getStringExtra("get_result").toString()
            Log.d("이메일", result)
            Log.d("이메일", "data!=null")
            val datas = makeGson.fromJson<MutableList<Result>>(result, listType.type)
            if (datas != null) {
                overviewList.addAll(datas)
                model.setResult(overviewList)
            }
            MainActivity.winGame = wingame
            MainActivity.loseGame = losegame
            model.setUserName(userName)
            model.setLoseGame(MainActivity.loseGame.toString())
            model.setWinGame(MainActivity.winGame.toString())
            editor.putString(SHARED_EMAIL, email)
            editor.putString(SHARED_RESULT, result)
            editor.putString(MainActivity.SHARED_NAME, userName)
            editor.putInt(SHARED_WIN, MainActivity.winGame)
            editor.putInt(SHARED_LOSE, MainActivity.loseGame)
        }
        editor.commit()
    }

}