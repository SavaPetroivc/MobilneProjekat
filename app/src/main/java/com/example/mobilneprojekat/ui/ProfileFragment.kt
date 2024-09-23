package com.example.mobilneprojekat.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.UserViewModel
import com.example.mobilneprojekat.data.Profile
import com.example.mobilneprojekat.data.Score
import com.google.firebase.firestore.auth.User

class ProfileFragment : Fragment() {

    private val viewModel = UserViewModel()
    private var userScore: Score? = null
    private var uid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val activity = requireActivity() as MainActivity
        uid = activity.getUid()
        viewModel.getScore(uid!!)

        val usernameText = view.findViewById<TextView>(R.id.usernameProf)
        val pointsText = view.findViewById<TextView>(R.id.pointsProf)

        viewModel.score.observe(viewLifecycleOwner, Observer { score ->
            if(score!=null) {
                userScore = score

                usernameText.text = userScore!!.username
                pointsText.text = userScore!!.points.toString()
            }
        })

        val logout = view.findViewById<Button>(R.id.logoutProf)

        logout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)

            startActivity(intent)
        }

        return view
    }

}