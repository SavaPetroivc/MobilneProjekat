package com.example.mobilneprojekat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var uid: String
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uid = intent.extras?.getString("uid")!!
        Log.d("Main", uid)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(MapFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Map -> replaceFragment(MapFragment())
                R.id.Leaderboard -> replaceFragment(LeaderboardFragment())
                R.id.Profile -> replaceFragment(ProfileFragment())
                R.id.SportEvents -> replaceFragment(SportEventsBoardFragment())

                else -> {

                }
            }

            true
        }

        fragmentManager = supportFragmentManager
    }

    private fun replaceFragment(fragment: Fragment){

        fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    fun getUid(): String {
        return uid
    }

    fun getFragmentManagerMA(): FragmentManager{
        return fragmentManager
    }
}