package com.example.basketballcount.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.basketballcount.R
import kotlinx.android.synthetic.main.fragment_overview.view.*


class OverviewFragment : Fragment() {
    private lateinit var wingameTv:TextView
    private lateinit var losegameTv:TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_overview, container, false)
        return view
    }
}