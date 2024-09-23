package com.example.mobilneprojekat.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.data.Score
import com.example.mobilneprojekat.data.ScoreDB

class LeaderboardAdapter(private val leaderboard: ArrayList<ScoreDB>): RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,
        parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return leaderboard.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = leaderboard[position]
        holder.positionText.text = (position+1).toString()+"."
        holder.usernameText.text = currentItem.username
        holder.pointsText.text = currentItem.points.toString()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val positionText: TextView = itemView.findViewById(R.id.positionLB)
        val usernameText: TextView = itemView.findViewById(R.id.usernameLB)
        val pointsText: TextView = itemView.findViewById(R.id.pointsLB)
    }
}