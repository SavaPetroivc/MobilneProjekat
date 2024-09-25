package com.example.mobilneprojekat.ui

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.data.PasswordEventDB

class PasswordEventsBoardAdapter(private val passwordEventDBS: ArrayList<PasswordEventDB>): RecyclerView.Adapter<PasswordEventsBoardAdapter.ViewHolder>() {

    private var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_passwordevents,
            parent, false)

        context = itemView.context

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return passwordEventDBS.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = passwordEventDBS[position]

        val geocoder = Geocoder(context!!)
        var addressText : MutableList<Address>

        addressText = geocoder.getFromLocation(currentItem.location.latitude, currentItem.location.longitude, 1)!!

        holder.ssidText.text = currentItem.ssid
        holder.authorText.text = currentItem.author

        if (addressText.isNotEmpty()) {
            holder.locationText.text = addressText[0].getAddressLine(0) + " " + addressText[0].getLocality()
        } else {
            holder.locationText.text = "Location not available"
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val ssidText: TextView = itemView.findViewById(R.id.ssidS)
        val authorText: TextView = itemView.findViewById(R.id.authorLS)
        val locationText: TextView = itemView.findViewById(R.id.locationLS)
    }
}