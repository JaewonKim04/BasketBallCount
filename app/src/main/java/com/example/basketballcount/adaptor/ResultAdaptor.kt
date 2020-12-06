package com.example.basketballcount.adaptor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basketballcount.R

class ResultAdaptor(results: MutableList<Result>) :
    RecyclerView.Adapter<ResultAdaptor.ViewHolder>() {
    private val items = results

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val winGameView = itemView.findViewById<TextView>(R.id.item_result_tv)
        val scoreView = itemView.findViewById<TextView>(R.id.item_record_tv)
        val awayNameView = itemView.findViewById<TextView>(R.id.item_away_tv)
        val gameDayView = itemView.findViewById<TextView>(R.id.item_day_tv)
        val gameTimeView = itemView.findViewById<TextView>(R.id.item_game_time_tv)
        fun setItem(item: Result) {
            if (item.win) {
                winGameView.setBackgroundColor(Color.BLUE)
                winGameView.text = "WIN"
            } else {
                winGameView.setBackgroundColor(Color.RED)
                winGameView.text = "LOSE"
            }
            scoreView.text = item.myScore.toString() + ":" + item.awayScore.toString()
            awayNameView.text = item.awayName
            gameDayView.text = item.Day
            if (item.time > 60) {
                gameTimeView.text = (item.time / 60).toString() + ":" + (item.time % 60).toString()
            } else {
                gameTimeView.text = "00:" + (item.time % 60).toString()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.record_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int=items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Result = items[position]
        holder.setItem(item)
    }

    fun addItem(item: Result) {
        items.add(item)
        notifyDataSetChanged()
    }
}