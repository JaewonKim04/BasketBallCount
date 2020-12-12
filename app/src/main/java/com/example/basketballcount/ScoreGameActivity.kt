package com.example.basketballcount

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.basketballcount.adaptor.Result
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_score_game.*
import kotlinx.android.synthetic.main.fragment_overview.*
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class ScoreGameActivity : AppCompatActivity() {

    var timerTask: Timer? = null
    var awayName: String = ""
    var gameType = true
    var myScore = 0
    var awayScore = 0
    var goalTime = 0
    var rememberTime = 0
    var oursize = 0
    var awaysize = 0
    val MY_SCORE = true
    val AWAY_SCORE = false
    var wingame = true
    private var goalScore by Delegates.notNull<Int>()
    var awayTeamList = mutableListOf<String>()
    var ourTeamList = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_game)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        val intent = intent
        goalScore = intent.getIntExtra("goal_score", 0)
        goalTime = intent.getIntExtra("goal_time", 0)
        gameType = intent.getBooleanExtra("game_type", true)
        awayName = intent.getStringExtra("away_name").toString()
        oursize = intent.getIntExtra("our_team_size", 0)
        awaysize = intent.getIntExtra("away_team_size", 0)
        val ourString: String? = intent.getStringExtra("our_team_list")
        val awayString: String? = intent.getStringExtra("away_team_list")
        val listType: TypeToken<MutableList<String>> = object : TypeToken<MutableList<String>>() {}
        val datas = MainActivity.makeGson.fromJson<MutableList<String>>(ourString, listType.type)
        val datasaway =
            MainActivity.makeGson.fromJson<MutableList<String>>(awayString, listType.type)
        awayTeamList.addAll(datasaway)
        ourTeamList.addAll(datas)
        away_team_tv.text = awayName
        if (gameType) {
            timerTask = timer(period = 1000, initialDelay = 1000) {
                goalTime += 1
                runOnUiThread {
                    setTime(goalTime)
                }
            }
        } else {
            setTime(goalTime)
            rememberTime = goalTime
            timerTask = timer(period = 1000, initialDelay = 1000) {
                goalTime -= 1
                if (goalTime <= 0) {
                    finishGame()
                }
                runOnUiThread {
                    setTime(goalTime)
                }
            }

        }
        finish_gmae_btn.setOnClickListener {
            finishGame()
        }

        my_plus_1_btn.setOnClickListener {
            myScore += 1
            checkFinish(myScore, MY_SCORE)
        }
        my_plus_2_btn.setOnClickListener {
            myScore += 2
            checkFinish(myScore, MY_SCORE)
        }
        my_plus_3_btn.setOnClickListener {
            myScore += 3
            checkFinish(myScore, MY_SCORE)
        }
        my_minus_1_btn.setOnClickListener {
            myScore -= 1
            checkFinish(myScore, MY_SCORE)
        }
        away_plus_1_btn.setOnClickListener {
            awayScore += 1
            checkFinish(awayScore, AWAY_SCORE)
        }
        away_plus_2_btn.setOnClickListener {
            awayScore += 2
            checkFinish(awayScore, AWAY_SCORE)
        }
        away_plus_3_btn.setOnClickListener {
            awayScore += 3
            checkFinish(awayScore, AWAY_SCORE)
        }
        away_minus_1_btn.setOnClickListener {
            awayScore -= 1
            checkFinish(awayScore, AWAY_SCORE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkFinish(score: Int, where: Boolean) {
        if (where) {
            my_score_et.setText(score.toString())
        } else {
            away_score_tv.setText(score.toString())
        }
        if (gameType) {
            if (score >= goalScore) {
                finishGame()
            }
        } else {
            if (goalTime <= 0) {
                finishGame()
                goalTime=1
            }
        }


    }

    private fun setTime(sec: Int) {
        var min = 0
        var second = sec
        if (second > 60) {
            min = second / 60
            second %= 60
        }

        time_tv.text = min.toString() + ":" + second.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun finishGame() {
        val day: String = LocalDate.now().toString()

        wingame = myScore > awayScore
        val intent = Intent()
        intent.putExtra("game_result", wingame)
        intent.putExtra("my_score", myScore)
        intent.putExtra("away_score", awayScore)
        intent.putExtra("away_name", awayName)
        intent.putExtra("our_team_size", oursize)
        intent.putExtra("away_team_size", awaysize)
        if (gameType) {
            intent.putExtra("game_time", goalTime)
        } else {
            intent.putExtra("game_time", rememberTime)
        }
        intent.putExtra("game_date", day)
        setResult(Activity.RESULT_OK, intent)
        setOtherPerson(wingame, oursize, awaysize)
        timerTask?.cancel()
        finish()
        return

    }

    private fun setOtherPerson(win: Boolean, oursize: Int, awaysize: Int) {
        if (oursize > 0 || awaysize > 0) {
            val oursize = oursize - 1
            val awaysize = awaysize - 1
            if (win) {
                for (ct in 0..oursize) {
                    var wingame: Long = 0
                    var result: String = ""
                    var losegame: Long = 0
                    val docRef = MainActivity.database.collection("users").document(ourTeamList[ct])
                    docRef.get().addOnSuccessListener { document ->
                        result = document.data?.get("result_gson") as String
                        losegame = document.data?.get("losegame") as Long
                        wingame = document.data?.get("wingame") as Long
                        val user = hashMapOf(
                            "wingame" to ++wingame,
                            "result_gson" to result,
                            "losegame" to losegame
                        )
                        MainActivity.database.collection("users").document(ourTeamList[ct])
                            .set(user).addOnSuccessListener {}
                    }

                }
                for (ct in 0..awaysize) {
                    var wingame: Long = 0
                    var result: String = ""
                    var losegame: Long = 0
                    val docRef =
                        MainActivity.database.collection("users").document(awayTeamList[ct])
                    docRef.get().addOnSuccessListener { document ->
                        result = document.data?.get("result_gson") as String
                        losegame = document.data?.get("losegame") as Long
                        wingame = document.data?.get("wingame") as Long
                        val user = hashMapOf(
                            "wingame" to wingame,
                            "losegame" to ++losegame,
                            "result_gson" to result
                        )
                        MainActivity.database.collection("users").document(awayTeamList[ct])
                            .set(user).addOnSuccessListener {}
                    }

                }
            } else {
                for (ct in 0..oursize) {
                    var wingame: Long = 0
                    var result: String = ""
                    var losegame: Long = 0
                    val docRef = MainActivity.database.collection("users").document(ourTeamList[ct])
                    docRef.get().addOnSuccessListener { document ->
                        result = document.data?.get("result_gson") as String
                        losegame = document.data?.get("losegame") as Long
                        wingame = document.data?.get("wingame") as Long
                        val user = hashMapOf(
                            "wingame" to wingame,
                            "losegame" to ++losegame,
                            "result_gson" to result
                        )
                        MainActivity.database.collection("users").document(ourTeamList[ct])
                            .set(user).addOnSuccessListener {}
                    }

                }
                for (ct in 0..awaysize) {
                    var wingame: Long = 0
                    var result: String = ""
                    var losegame: Long = 0
                    val docRef =
                        MainActivity.database.collection("users").document(awayTeamList[ct])
                    docRef.get().addOnSuccessListener { document ->
                        wingame = document.data?.get("wingame") as Long
                        result = document.data?.get("result_gson") as String
                        losegame = document.data?.get("losegame") as Long
                        val user = hashMapOf(
                            "wingame" to ++wingame,
                            "result_gson" to result,
                            "losegame" to losegame
                        )
                        MainActivity.database.collection("users").document(awayTeamList[ct])
                            .set(user).addOnSuccessListener {}
                    }

                }
            }

        }


    }

}