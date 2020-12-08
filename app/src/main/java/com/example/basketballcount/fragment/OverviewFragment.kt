package com.example.basketballcount.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.basketballcount.R
import com.example.basketballcount.WinGameViewModel
import kotlinx.android.synthetic.main.fragment_overview.*


class OverviewFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model=ViewModelProvider(requireActivity()).get(WinGameViewModel::class.java)
        model.wingame.observe(viewLifecycleOwner, Observer {
            win_tv.text=it
        })
        model.userName.observe(viewLifecycleOwner, Observer {
            name_tv.text=it
        })
    }
}