package com.example.swcapstone

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NumberSettingActivity : AppCompatActivity() {
    private lateinit var editTextNumber: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_targetcalorific)

        editTextNumber = findViewById(R.id.goal_calories_input)
        saveButton = findViewById(R.id.save_button)

        val prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val lastNumber = prefs.getInt("lastNumber", 0)  // 초기값은 0입니다.

        saveButton.setOnClickListener {
            val number = editTextNumber.text.toString().toIntOrNull()
            if (number != null) {
                var newTotal = lastNumber + number  // 현재 입력된 숫자를 이전 숫자에 더함
                if (newTotal > 10000) {  // 총합이 10,000을 넘을 경우
                    newTotal = 0  // 총합을 0으로 초기화
                }
                prefs.edit().putInt("lastNumber", newTotal).apply()  // 수정된 총합을 저장
                finish()  // Activity 종료
            } else {
                editTextNumber.error = "Valid number required"  // 유효한 숫자 입력 요구
            }
        }
    }
}