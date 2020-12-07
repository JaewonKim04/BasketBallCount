package com.example.basketballcount.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.basketballcount.R
import com.example.basketballcount.WingameViewModel
import kotlinx.android.synthetic.main.fragment_overview.view.*


class OverviewFragment : Fragment() {
    private lateinit var wingameTv:TextView
    private lateinit var losegameTv:TextView
    private lateinit var model: WingameViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_overview, container, false)
        wingameTv=view.win_tv
        losegameTv=view.lose_tv
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    //나중에 Live데이터로 overview set 하기
}