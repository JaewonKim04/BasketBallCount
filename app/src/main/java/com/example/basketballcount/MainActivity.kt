package com.example.basketballcount

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.basketballcount.adaptor.Result
import com.example.basketballcount.adaptor.ResultAdaptor
import com.example.basketballcount.fragment.OverviewFragment
import com.example.basketballcount.fragment.SearchFragment
import com.example.basketballcount.fragment.StartGameFragment
import com.example.basketballcount.login.GetUserNameActivity
import com.example.basketballcount.viewmodel.WinGameViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_LOGIN = 1
    private var userName: String = ""

    companion object {
        lateinit var model: WinGameViewModel
        val makeGson = GsonBuilder().create()
        val listType: TypeToken<MutableList<Result>> = object : TypeToken<MutableList<Result>>() {}
        val database = FirebaseFirestore.getInstance()
        var winGame = 0
        var loseGame = 0
        val overviewList = mutableListOf<Result>()
        lateinit var result: ResultAdaptor
        lateinit var startShared: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        val SHARED_NAME = "get_id"
        val SHARED_WIN = "win_game"
        val SHARED_LOSE = "lose_game"
        val SHARED_RESULT = "game_result"
        val SHARED_EMAIL = "get_email_fire"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startShared = getSharedPreferences("auto_login", Context.MODE_PRIVATE)
        editor = startShared.edit()
        result = ResultAdaptor(overviewList)
        startObserve()
        model = ViewModelProvider(this).get(WinGameViewModel::class.java)
        FirebaseApp.initializeApp(this)
        bottom_navigation.setOnNavigationItemSelectedListener(itemListener)
        if (readAutoLogin()) {
            val intent = Intent(this, GetUserNameActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        }
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            OverviewFragment()
        )
            .commit()
    }

    private fun startObserve() {
        val model = ViewModelProvider(this).get(WinGameViewModel::class.java)
        model.wingame.observe(this, Observer {
            editor.putInt(SHARED_WIN, it.toInt())
            editor.apply()
        })
        model.losegame.observe(this, Observer {
            editor.putInt(SHARED_LOSE, it.toInt())
            editor.apply()
        })
    }

    private val itemListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.menu_overview -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    OverviewFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_search -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    SearchFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_start -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    StartGameFragment()
                ).commit()
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                return@OnNavigationItemSelectedListener false
            }
        }

    }

    private fun readAutoLogin(): Boolean {
        if (startShared.getString(SHARED_NAME, null) != null) {
            userName = startShared.getString(SHARED_NAME, "")!!
            winGame = startShared.getInt(SHARED_WIN, 0)
            loseGame = startShared.getInt(SHARED_LOSE, 0)
            overviewList.clear()
            val getRecycler = startShared.getString(SHARED_RESULT, "")
            val datas = makeGson.fromJson<MutableList<Result>>(getRecycler, listType.type)
            if (datas != null) {
                overviewList.addAll(datas)
                model.setResult(overviewList)
            }
            else{
                overviewList.clear()
                model.setResult(overviewList)
            }
            model.setUserName(userName)
            model.setWinGame(winGame.toString())
            model.setLoseGame(loseGame.toString())
            return false
        }
        return true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            Log.d("읽어옴?","ㅇㅇ")
            var result = ""
            var email = ""
            var wingame = 0
            var losegame = 0
            userName = data!!.getStringExtra("get_name").toString()
            email = data.getStringExtra("get_email").toString()
            wingame = data.getLongExtra("get_win_fire", 0).toInt()
            losegame = data.getLongExtra("get_lose_fire", 0).toInt()
            result = data.getStringExtra("get_result").toString()
            val datas = makeGson.fromJson<MutableList<Result>>(result, listType.type)
            if (datas != null) {
                overviewList.addAll(datas)
                model.setResult(overviewList)
            }
            winGame = wingame
            loseGame = losegame
            model.setUserName(userName)
            model.setLoseGame(loseGame.toString())
            model.setWinGame(winGame.toString())
            model.setEmail(email)
            editor.putString(SHARED_EMAIL, email)
            editor.putString(SHARED_RESULT, result)
            editor.putString(SHARED_NAME, userName)
            editor.putInt(SHARED_WIN, winGame)
            editor.putInt(SHARED_LOSE, loseGame)
        }
        editor.apply()
    }

}