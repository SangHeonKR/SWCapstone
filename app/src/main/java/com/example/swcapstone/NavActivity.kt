package com.example.swcapstone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    true
                }
                R.id.navigation_community -> {
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    true
                }
                R.id.navigation_myhome -> {
                    startActivity(Intent(this, MyPageActivity::class.java))
                    true
                }
                R.id.navigation_record -> {
                    startActivity(Intent(this, RecordActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
