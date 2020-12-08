package com.example.basketballcount.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basketballcount.MainActivity.Companion.overviewList
import com.example.basketballcount.MainActivity.Companion.result
import com.example.basketballcount.R
import com.example.basketballcount.WinGameViewModel
import com.example.basketballcount.adaptor.ResultAdaptor
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
        val model = ViewModelProvider(requireActivity()).get(WinGameViewModel::class.java)
        result_recyclerview.adapter=result
        result_recyclerview.layoutManager=LinearLayoutManager(requireActivity())
        model.wingame.observe(viewLifecycleOwner, Observer {
            win_tv.text = it
        })
        model.userName.observe(viewLifecycleOwner, Observer {
            name_tv.text = it
        })
        model.losegame.observe(viewLifecycleOwner, Observer {
            lose_tv.text = it
        })
        model.resultRecyclerView.observe(viewLifecycleOwner, Observer {
            overviewList.add(result.itemCount,it)
            Log.d("itemCount",result.itemCount.toString())
            //result.addItem(it)
        })
    }

}