package com.example.mobilneprojekat.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.data.ScoreDB

class SportEventAdapter(private val users: List<String>): RecyclerView.Adapter<SportEventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_users,
            parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = users[position]
        holder.usernameText.text = currentItem
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val usernameText: TextView = itemView.findViewById(R.id.usernameLU)
    }
}