package com.example.basketballcount.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.basketballcount.R
import com.example.basketballcount.ScoreGameActivity
import com.example.basketballcount.TimeGameActivity
import kotlinx.android.synthetic.main.activity_get_user_name.*
import kotlinx.android.synthetic.main.fragment_start_game.*
import kotlinx.android.synthetic.main.fragment_start_game.view.*

class StartGameFragment : Fragment() {
    var startScoreGame: Int = 1
    var getAway = false
    var getMin = false
    var getSec = false
    var getScore = false
    var readyToStart = false
    private lateinit var startButton: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start_game, container, false)
        startButton = view.findViewById(R.id.start_game_btn)
        startButton.setOnClickListener {
            if(readyToStart){
                if (startScoreGame == 1) {
                    val intent = Intent(context, ScoreGameActivity::class.java)
                    startActivityForResult(intent, 1)
                } else {
                    val intent = Intent(context, TimeGameActivity::class.java)
                    startActivityForResult(intent, 2)
                }
            }
            else{
                Toast.makeText(context,"게임세팅을 해주세요",Toast.LENGTH_SHORT).show()
            }

        }
        view.choose_mode_btn {
            initialCheckedIndex = 1
            initWithItems {
                listOf("점수", "시간")
            }
            onSegmentChecked { segment ->
                if (segment.text == "점수") {
                    startScoreGame = 1
                    view.decide_min_et.visibility = View.INVISIBLE
                    view.decide_sec_et.visibility = View.INVISIBLE
                    view.decide_score_et.visibility = View.VISIBLE
                    getMin = false
                    getScore = false
                    getSec = false
                    readyToStart()
                } else if (segment.text == "시간") {
                    startScoreGame = 2
                    view.decide_min_et.visibility = View.VISIBLE
                    view.decide_sec_et.visibility = View.VISIBLE
                    view.decide_score_et.visibility = View.INVISIBLE
                    getMin = false
                    getScore = false
                    getSec = false
                    readyToStart()
                }
            }
            onSegmentUnchecked {
                startScoreGame = 1
            }
            onSegmentRechecked { segment ->
                if (segment.text == "점수") {
                    startScoreGame = 1
                    view.decide_min_et.visibility = View.INVISIBLE
                    view.decide_sec_et.visibility = View.INVISIBLE
                    view.decide_score_et.visibility = View.VISIBLE
                    getMin = false
                    getScore = false
                    getSec = false
                    readyToStart()
                } else if (segment.text == "시간") {
                    startScoreGame = 2
                    view.decide_min_et.visibility = View.VISIBLE
                    view.decide_sec_et.visibility = View.VISIBLE
                    view.decide_score_et.visibility = View.INVISIBLE
                    getMin = false
                    getScore = false
                    getSec = false
                    readyToStart()
                }
            }

        }
        view.get_away_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getAway = view.get_away_et.text.toString().isNotEmpty()
                readyToStart()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getAway = count > 0
                readyToStart()
            }
        })
        view.decide_score_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getScore = view.decide_score_et.text.toString().isNotEmpty()
                readyToStart()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getScore = count > 0
                readyToStart()
            }
        })
        view.decide_min_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getMin = view.decide_min_et.text.toString().isNotEmpty()
                readyToStart()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getMin = count > 0
                readyToStart()
            }
        })
        view.decide_sec_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getSec = view.decide_sec_et.text.toString().isNotEmpty()
                readyToStart()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getSec = count > 0
                readyToStart()
            }
        })
        return view
    }

    private fun readyToStart() {
        readyToStart = if (getAway && ((getMin && getSec) || getScore)) {
            startButton.setBackgroundColor(Color.GREEN)
            true
        } else {
            startButton.setBackgroundColor(Color.GRAY)
            false
        }
    }


}