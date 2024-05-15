package com.example.swcapstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Make sure this matches your layout file name

        // Initialize any UI components and set up listeners here
        // For example, setup a button click listener, etc.
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

    }

    // Additional methods for handling user interactions can be added here
}
