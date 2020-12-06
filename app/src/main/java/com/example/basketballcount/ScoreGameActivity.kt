package com.example.basketballcount

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_score_game.*
import java.util.*
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class ScoreGameActivity : AppCompatActivity() {

    var timerTask: Timer?=null
    var gameType=true
    var myScore=0
    var awayScore=0
    var goalTime=0
    val MY_SCORE=true
    val AWAY_SCORE=false
    var wingame=true
    private var goalScore by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score_game)
        val intent= intent
        goalScore=intent.getIntExtra("goal_score",0)
        goalTime=intent.getIntExtra("goal_time",0)
        gameType=intent.getBooleanExtra("game_type",true)
        if(!gameType){
            timerTask=timer(period = 1000,initialDelay = 1000){
                goalTime+=1
                runOnUiThread {
                    setTime(goalTime)
                }
            }
        }
        else{timerTask=timer(period = 1000,initialDelay = 1000){
            goalTime-=1
            runOnUiThread {
                setTime(goalTime)
            }
        }

        }


        my_plus_1_btn.setOnClickListener {
            myScore+=1
            checkFinish(myScore,MY_SCORE)
        }
        my_plus_2_btn.setOnClickListener {
            myScore+=2
            checkFinish(myScore,MY_SCORE)
        }
        my_plus_3_btn.setOnClickListener {
            myScore+=3
            checkFinish(myScore,MY_SCORE)
        }
        my_minus_1_btn.setOnClickListener {
            myScore-=1
            checkFinish(myScore,MY_SCORE)
        }
        away_plus_1_btn.setOnClickListener {
            awayScore+=1
            checkFinish(awayScore,AWAY_SCORE)
        }
        away_plus_2_btn.setOnClickListener {
            awayScore+=2
            checkFinish(awayScore,AWAY_SCORE)
        }
        away_plus_3_btn.setOnClickListener {
            awayScore+=3
            checkFinish(awayScore,AWAY_SCORE)
        }
        away_minus_1_btn.setOnClickListener {
            awayScore-=1
            checkFinish(awayScore,AWAY_SCORE)
        }

    }
    private fun checkFinish(score:Int,where:Boolean){
        if(where){
            my_score_et.setText(score.toString())
        }
        else{
            away_score_tv.setText(score.toString())
        }
        if(gameType){
            if(score>goalScore){
                wingame = myScore>awayScore
                //끝났을때 코드
            }
        }
        else{
            if(goalTime<=0){
                wingame=myScore>awayScore
                //끝났을때코드
            }
        }


    }
    private fun setTime(sec:Int){
        var min=0
        var second=sec
        if(second>60){
            min=second/60
            second %= 60
        }

        time_tv.setText(min.toString()+":"+second.toString())
    }
}