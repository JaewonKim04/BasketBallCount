package com.example.basketballcount

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.basketballcount.fragment.OverviewFragment
import com.example.basketballcount.fragment.SearchFragment
import com.example.basketballcount.fragment.StartGameFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_LOGIN = 1
    private val REQUEST_CODE_GAME = 3
    private var userName: String = ""
    private var winGame: Int = 0
    private var loseGame: Int = 0
    var resultWinGame = true
    var resultMyScore = 0
    var resultAwayScore = 0
    var resultAwayName = ""
    var resultGameTime = 0

    //나중에 Live데이터로 overview set 하기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        val startShared: SharedPreferences =
            getSharedPreferences("auto_login", Context.MODE_PRIVATE)
        if (startShared.getString("get_id", null) == null) {
            return true
        }
        startShared.getString("get_id", userName)
        startShared.getInt("win_game", winGame)
        startShared.getInt("lose_game", loseGame)
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val getShared: SharedPreferences =
            getSharedPreferences("auto_login", Context.MODE_PRIVATE)
        val editor = getShared.edit()
        if (requestCode == REQUEST_CODE_LOGIN) {
            userName = data?.getStringExtra("get_name").toString()
            winGame = 0
            loseGame = 0
            editor.putString("get_id", userName)
            editor.putInt("get_win", winGame)
            editor.putInt("get_lose", loseGame)
        } else if (requestCode == REQUEST_CODE_GAME) {
            resultAwayName = data!!.getStringExtra("away_name").toString()
            resultMyScore=data.getIntExtra("my_score",0)
            resultAwayScore=data.getIntExtra("away_score",0)
            resultGameTime=data.getIntExtra("game_time",0)
            resultWinGame=data.getBooleanExtra("game_result",true)
        }
        editor.apply()
    }

}