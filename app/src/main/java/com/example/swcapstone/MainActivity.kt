package com.example.swcapstone

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.swcapstone.R.id
import com.example.swcapstone.R.layout
import com.google.firebase.auth.FirebaseAuth

/*
    현재 MainActivity.kt 가 activity_welcome.xml 랑 연결되어있음
    후일에 본 파일이 메인화면으로 넘어가게 끔 수정될 예정
        - 또는 로그인 완료 후 짧게 곱창마카롱 로고가 흰 배경에 팝업한 후 메인화면으로
          넘어가는 방안도 검토 바람
*/

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_welcome)

        auth = FirebaseAuth.getInstance()

        val welcomeText: TextView = findViewById(id.welcomeText)
        val logoutButton: Button = findViewById(id.logoutButton)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            welcomeText.text = "Welcome, ${currentUser.email}"
        } else {
            // Redirect to Login Activity if not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainScreenActivity::class.java))
            finish()
        }, 3000)  // 3000 milliseconds or 3 seconds


        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}