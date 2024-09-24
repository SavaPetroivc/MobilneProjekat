package com.example.mobilneprojekat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.example.mobilneprojekat.R
import java.util.Calendar
import java.util.Date

class FilterFragment : Fragment() {

    private var authorFilter: String = ""
    private var passwordTypesFilter: ArrayList<String> = ArrayList<String>()
    private var bandwidthFilter: String = ""
    private var date : Date? = null
    private var radius: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authorFilter = arguments?.getString("author")!!
        passwordTypesFilter = arguments?.getStringArrayList("PasswordTypes")!!
        bandwidthFilter = arguments?.getString("bandwidth")!!
        date = arguments?.getSerializable("date") as? Date
        radius = arguments?.getDouble("radius")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_filter, container, false)

        val authorInput = view.findViewById<TextView>(R.id.authorsAF)
        authorInput.text = authorFilter
        val publicCheck = view.findViewById<CheckBox>(R.id.publicAF)
        if("Public" in passwordTypesFilter)
            publicCheck.isChecked = true
        val privateCheck = view.findViewById<CheckBox>(R.id.privateAF)
        if("Private" in passwordTypesFilter)
            privateCheck.isChecked = true
        val bandwidth5Check = view.findViewById<CheckBox>(R.id.Bandwidth5ghzAF)
        if("5Ghz" == bandwidthFilter)
            bandwidth5Check.isChecked = true
        val bandwidth2Check = view.findViewById<CheckBox>(R.id.Bandwidht2AF)
        if("2.4Ghz" == bandwidthFilter)
            bandwidth2Check.isChecked = true
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        if(date!=null)
            calendarView.date = date!!.time
        val radiusNumber = view.findViewById<EditText>(R.id.radiusAF)
        if(radius!=0.0)
            radiusNumber.setText(radius.toString())

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            date = calendar.time
        }

        val addFilter = view.findViewById<Button>(R.id.addFilters)

        addFilter.setOnClickListener{
            passwordTypesFilter = ArrayList<String>()

            if(privateCheck.isChecked)
                passwordTypesFilter.add("Private")
            if(publicCheck.isChecked)
                passwordTypesFilter.add("Public")

            authorFilter = authorInput.text.toString()

            if(bandwidth5Check.isChecked && !bandwidth2Check.isChecked)
                bandwidthFilter = "5Ghz"
            else if(!bandwidth5Check.isChecked && bandwidth2Check.isChecked)
                bandwidthFilter = "2.4Ghz"
            else
                bandwidthFilter = ""
            if(radiusNumber.text.isNotEmpty())
                radius = radiusNumber.text.toString().toDoubleOrNull()!!
            else
                radius = 0.0

            val fragment = MapFragment()

            val bundle = Bundle()
            bundle.putString("author", authorFilter)
            bundle.putStringArrayList("PasswordTypes",passwordTypesFilter)
            bundle.putString("bandwidth", bandwidthFilter)
            if(date!=null)
                bundle.putSerializable("date", date)
            bundle.putDouble("radius", radius)
            fragment.arguments = bundle

            val activity = requireActivity() as MainActivity
            val fragmentManager = activity.getFragmentManagerMA()
            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.frame_layout, fragment)
            transaction.addToBackStack("filter")
            transaction.commit()
        }

        val returnButton = view.findViewById<Button>(R.id.returnAF)

        returnButton.setOnClickListener {
            getParentFragmentManager().popBackStack()
        }

        return view
    }

}