package com.example.mobilneprojekat.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.UserViewModel
import com.example.mobilneprojekat.data.Profile
import com.example.mobilneprojekat.data.PasswordEvent
import com.google.firebase.firestore.GeoPoint
import java.util.Calendar

class CreatePasswordEventFragment : Fragment() {

    private var viewModel: UserViewModel = UserViewModel()
    private var userProfile: Profile? = null
    private lateinit var wifiType: String
    private lateinit var passwordTypes: Array<String>
    private var lat: Double? = null
    private var lng: Double? = null
    private lateinit var location: GeoPoint
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = requireActivity() as MainActivity
        uid = activity.getUid()
        viewModel.getProfile(uid!!)

        viewModel.userProfile.observe(this, Observer { profile ->
            if(profile!=null)
                userProfile = profile//Profile(profile.username, profile.name, profile.lastname, profile.phone)
        })

        passwordTypes = resources.getStringArray(R.array.PasswordTypes)

        lat = arguments?.getDouble("lat")
        lng = arguments?.getDouble("lng")

        location = GeoPoint(lat!!, lng!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_password_event, container, false)

        var addPasswordEvent = view.findViewById<Button>(R.id.addPasswordEventCSE)
        var passwordSpinner = view.findViewById<Spinner>(R.id.spinnerCSE)

        passwordSpinner.adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, passwordTypes)

        passwordSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                wifiType = "public"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                wifiType = passwordTypes[position]
            }
        }


        addPasswordEvent.setOnClickListener{
            val bandwidthCheckBox = view.findViewById<CheckBox>(R.id.bandwidhtCSE)
            val bandwidth = bandwidthCheckBox.isChecked
            val passwordText = view.findViewById<TextView>(R.id.passwordCSE)
            val password = passwordText.text.toString()
            val ssidText = view.findViewById<TextView>(R.id.ssidTextCSE)
            val ssid = ssidText.text.toString()

            val date = com.google.firebase.Timestamp(Calendar.getInstance().time)
            val used = listOf<String>(userProfile!!.username)

            val passwordEvent = PasswordEvent(wifiType, bandwidth, password, ssid, dateAdded = date, location, userProfile!!.username, used)

            Log.d("CSE UID", uid!!)
            //viewModel.updateProfile(uid!!)
            viewModel.addPasswordEvent(passwordEvent)
            viewModel.updateScore(10.0, uid!!) //Ne RAdI
        }

        val returnButton = view.findViewById<Button>(R.id.returnCSE)

        returnButton.setOnClickListener {
            getParentFragmentManager().popBackStack()
        }

        viewModel.addedPasswordEvent.observe(viewLifecycleOwner, Observer { added ->
            if(added != null && added == true)
                getParentFragmentManager().popBackStack()
        })

        return view
    }

}