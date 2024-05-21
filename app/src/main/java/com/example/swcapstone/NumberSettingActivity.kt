package com.example.swcapstone

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NumberSettingActivity : AppCompatActivity() {
    private lateinit var editTextNumber: EditText
    private lateinit var saveButton: Button
    private lateinit var displayTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_targetcalorific)

        editTextNumber = findViewById(R.id.goal_calories_input)
        saveButton = findViewById(R.id.save_button)
        displayTextView = findViewById(R.id.goal_calories_display)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        var lastNumber = prefs.getInt("lastNumber", 0)  // 초기값은 0입니다.
        var goalCalories = prefs.getInt("goalCalories", 2000) // 목표 칼로리 기본값 2000

        // 초기화시 기존 값을 표시
        updateDisplay(lastNumber, goalCalories)

        saveButton.setOnClickListener {
            val number = editTextNumber.text.toString().toIntOrNull()
            if (number != null) {
                if (number == 8888) {
                    // 관리자 옵션: 섭취 칼로리 초기화
                    lastNumber = 0
                    prefs.edit().putInt("lastNumber", lastNumber).apply()
                } else {
                    // 목표 칼로리 설정
                    goalCalories = number
                    prefs.edit().putInt("goalCalories", goalCalories).apply()
                }
                updateDisplay(lastNumber, goalCalories)  // TextView 업데이트
                finish()  // Activity 종료
            } else {
                editTextNumber.error = "Valid number required"  // 유효한 숫자 입력 요구
            }
        }
    }

    private fun updateDisplay(currentCalories: Int, goalCalories: Int) {
        displayTextView.text = "오늘의 섭취 칼로리: $currentCalories / $goalCalories"
    }
}
