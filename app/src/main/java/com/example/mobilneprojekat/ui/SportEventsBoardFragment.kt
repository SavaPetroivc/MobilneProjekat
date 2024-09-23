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
import com.example.mobilneprojekat.data.PasswordEventDB

class SportEventsBoardFragment : Fragment() {

    private lateinit var adapter: SportEventsBoardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sportEvents: ArrayList<PasswordEventDB>

    private var viewModel: UserViewModel = UserViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sport_events_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPasswordEvents()

        viewModel.passwordEvents.observe(viewLifecycleOwner, Observer { sportEv ->
            if(sportEv != null) {
                sportEvents = sportEv
                val layoutManager = LinearLayoutManager(context)
                recyclerView = view.findViewById(R.id.sportEventsBoardRV)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                adapter = SportEventsBoardAdapter(sportEvents)
                recyclerView.adapter = adapter
            }
        })
    }
}