package com.example.mobilneprojekat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.UserViewModel
import com.example.mobilneprojekat.data.Profile
import com.example.mobilneprojekat.data.PasswordEvent
import com.google.android.material.textfield.TextInputEditText

class SportEventFragment : Fragment() {

    private var passwordEventId: String = ""
    private var viewModel = UserViewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SportEventAdapter
    private var userProfile: Profile? = null
    private var uid: String? = null
    private var passwordEvent: PasswordEvent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        passwordEventId = arguments?.getString("passwordEventId")!!
        viewModel.getPasswordEvent(passwordEventId)

        val activity = requireActivity() as MainActivity
        uid = activity.getUid()
        viewModel.getProfile(uid!!)

        viewModel.userProfile.observe(this, Observer { profile ->
            if(profile!=null)
                userProfile = profile
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sport_event, container, false)

        val ssidText = view.findViewById<TextView>(R.id.ssidSEF)
        val passwordText = view.findViewById<TextView>(R.id.passwordSEF)
        val wifiTypeText = view.findViewById<TextView>(R.id.typeSEF)
        val authorText = view.findViewById<TextView>(R.id.authorSEF)
        val bandwidthText = view.findViewById<TextView>(R.id.bandwidthSEF)
        //val descriptionText = view.findViewById<TextView>(R.id.descriptionSEF)

        viewModel.passwordEvent.observe(viewLifecycleOwner, Observer { passwordEv ->
            if(passwordEv != null){

                passwordEvent = passwordEv

                ssidText.text=passwordEv.ssid
                passwordText.text = passwordEv.password
                wifiTypeText.text = passwordEv.wifiType
                authorText.text = passwordEv.author
                if(passwordEv.bandwidth)
                    bandwidthText.text = "5 Ghz"
                else
                    bandwidthText.text = "2.4 Ghz"
                //descriptionText.text = passwordEv.password

                val users = passwordEv.used
                val layoutManager = LinearLayoutManager(context)
                recyclerView = view.findViewById(R.id.usersSEF)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                adapter = SportEventAdapter(users)
                recyclerView.adapter = adapter
            }
        })

        val change = view.findViewById<Button>(R.id.changeSEF)
        val newPassword = view.findViewById<TextInputEditText>(R.id.newPasswordTXT)

        change.setOnClickListener{
            if(userProfile!=null){
                viewModel.updatePassword(newPassword.text.toString(), passwordEventId)
                viewModel.joinEvent(userProfile!!.username, passwordEventId)
                viewModel.updateScore(5.0, uid!!)

            }
        }

        viewModel.changeEvent.observe(viewLifecycleOwner, Observer { joined ->
            if(joined != null && joined == true)
                getParentFragmentManager().popBackStack()
        })

        val returnButton = view.findViewById<Button>(R.id.returnSEF)

        returnButton.setOnClickListener {
            getParentFragmentManager().popBackStack()
        }

        return view
    }
}