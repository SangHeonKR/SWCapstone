package com.example.swcapstone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.Locale

class SearchFragment : Fragment() {

    private lateinit var caloriesTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // 로컬을 한국어로 설정
        val locale = Locale("ko", "KR")
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val calendarView: CalendarView = view.findViewById(R.id.cal)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val intent = Intent(activity, DetailActivity::class.java).apply {
                putExtra("YEAR", year)
                putExtra("MONTH", month)
                putExtra("DAY_OF_MONTH", dayOfMonth)
            }
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
