package com.example.swcapstone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_main, container, false)

        val calendarView: CalendarView = view.findViewById(R.id.cal)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val intent = Intent(activity, DetailActivity::class.java).apply {
                putExtra("YEAR", year)
                putExtra("MONTH", month)
                putExtra("DAY_OF_MONTH", dayOfMonth)
            }
            startActivity(intent)
        }

        // Find the cameraTestButton from activity_main layout
        val cameraTestButton: Button = view.findViewById(R.id.cameraTest)
        cameraTestButton.setOnClickListener {
            // Navigate to RecordActivity when cameraTestButton is clicked
            val intent = Intent(activity, RecordFragment::class.java)
            startActivity(intent)
        }

        return view
    }
}


