package com.example.basketballcount

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_score_game.*
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
    val MY_SCORE = true
    val AWAY_SCORE = false
    var wingame = true
    private var goalScore by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_game)
        val intent = intent
        goalScore = intent.getIntExtra("goal_score", 0)
        goalTime = intent.getIntExtra("goal_time", 0)
        gameType = intent.getBooleanExtra("game_type", true)
        awayName = intent.getStringExtra("user_name").toString()
        textView7.text = awayName
        Log.d("타임", goalTime.toString())
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
                if(goalTime<=0){
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

    private fun checkFinish(score: Int, where: Boolean) {
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
    private fun finishGame(){
        if (gameType) {
            wingame = myScore > awayScore
            val intent = Intent()
            intent.putExtra("game_result", wingame)
            intent.putExtra("my_score", myScore)
            intent.putExtra("away_score", awayScore)
            intent.putExtra("game_time", goalTime)
            intent.putExtra("away_name", awayName)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            wingame = myScore > awayScore
            val intent = Intent()
            intent.putExtra("game_result", wingame)
            intent.putExtra("my_score", myScore)
            intent.putExtra("away_score", awayScore)
            intent.putExtra("game_time", goalTime)
            intent.putExtra("away_name", rememberTime)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}