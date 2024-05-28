package com.example.swcapstone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {

    private lateinit var caloriesTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

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
            val intent = Intent(activity, CameraActivity::class.java)
            startActivity(intent)
        }

        caloriesTextView = view.findViewById(R.id.calorieText)

        displayTotalCalories()

        return view
    }

    private fun displayTotalCalories() {
        val prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val totalCalories = prefs.getInt("totalCalories", 0)

        caloriesTextView.text = "${totalCalories}kcal"
    }
}


