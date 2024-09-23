package com.example.mobilneprojekat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.UserViewModel
import com.example.mobilneprojekat.data.Score
import com.example.mobilneprojekat.data.ScoreDB

class LeaderboardFragment : Fragment() {

    private lateinit var adapter: LeaderboardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var leaderboard: ArrayList<ScoreDB>

    private var viewModel: UserViewModel = UserViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLeaderboard()

        viewModel.leaderboard.observe(viewLifecycleOwner, Observer { leaderb ->
            if(leaderb != null) {
                leaderboard = leaderb
                val layoutManager = LinearLayoutManager(context)
                recyclerView = view.findViewById(R.id.leaderboardRV)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                adapter = LeaderboardAdapter(leaderboard)
                recyclerView.adapter = adapter
            }
        })

    }

}