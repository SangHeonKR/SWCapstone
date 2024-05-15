package com.example.swcapstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.swcapstone.R
import java.util.Locale
import android.content.res.Configuration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        val locale = Locale("ko")
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)

        val navView: BottomNavigationView = findViewById(R.id.nav)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    loadFragment(SearchFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_community -> {
                    loadFragment(CommunityFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_myhome -> {
                    loadFragment(MyHomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_record -> {
                    loadFragment(RecordFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        if (savedInstanceState == null) {
            navView.selectedItemId = R.id.navigation_search
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_control, fragment)
            .commit()
    }
}
