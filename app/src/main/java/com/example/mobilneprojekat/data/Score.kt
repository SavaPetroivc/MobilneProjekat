package com.example.mobilneprojekat.data

import java.io.Serializable

data class Score(
    val username: String = "",
    val points: Long = 0
)

class ScoreDB(var id: String, var username: String, var points: Long) : Serializable{
    constructor():this("","",0)
}
