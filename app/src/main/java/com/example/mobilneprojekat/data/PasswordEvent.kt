package com.example.mobilneprojekat.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

data class PasswordEvent(
    val wifiType: String = "",
    val bandwidth: Boolean = true, //5ghz
    val password: String = "",
    val ssid:String ="",
    val dateAdded: Timestamp = Timestamp.now(),
    val location: GeoPoint = GeoPoint(0.0,0.0),
    val author: String = "",
    val used: List<String> = listOf()
)

class PasswordEventDB(var id: String,
                      var wifiType: String,
                      val bandwidth: Boolean,
                      val password: String,
                      val ssid:String,
                      val dateAdded: Timestamp,
                      val location: GeoPoint,
                      val author: String,
                      val used: List<String>) : Serializable {
    constructor():this("","",false, "", "", Timestamp.now(),
        GeoPoint(0.0,0.0), "", emptyList()
    )
}