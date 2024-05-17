package com.example.swcapstone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val year = intent.getIntExtra("YEAR", -1)
        val month = intent.getIntExtra("MONTH", -1)
        val dayOfMonth = intent.getIntExtra("DAY_OF_MONTH", -1)

        // Locale 설정에 따라 날짜 형식화
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        dateTextView.text = dateFormat.format(calendar.time)

        // Set up the button click listener to navigate to CameraActivity
        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}
