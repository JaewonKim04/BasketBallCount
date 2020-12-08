package com.example.basketballcount

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.basketballcount.fragment.OverviewFragment
import com.example.basketballcount.fragment.SearchFragment
import com.example.basketballcount.fragment.StartGameFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_LOGIN = 1
    private var userName: String = ""
    companion object{
        lateinit var startShared: SharedPreferences
        lateinit var editor :SharedPreferences.Editor
        var winGame= 0
        var loseGame= 0
    }


    //나중에 Live데이터로 overview set 하기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        startShared =
            getSharedPreferences("auto_login", Context.MODE_PRIVATE)
        editor= startShared.edit()
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            OverviewFragment()
        )
            .commit()
        bottom_navigation.setOnNavigationItemSelectedListener(itemListener)
        if (readAutoLogin()) {
            val intent = Intent(this, GetUserNameActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        }
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
        if (startShared.getString("get_id", null) != null) {
            startShared.getString("get_id", userName)
            startShared.getInt("win_game", winGame)
            startShared.getInt("lose_game", loseGame)
            return false
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            userName = data?.getStringExtra("get_name").toString()
            Log.d("이름",userName)
            winGame = 0
            loseGame = 0
            editor.putString("get_id", userName)
            editor.putInt("win_game", winGame)
            editor.putInt("lose_game", loseGame)
        }
        editor.apply()
    }

}