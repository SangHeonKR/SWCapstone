package com.example.swcapstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Make sure this matches your layout file name

        // Initialize any UI components and set up listeners here
        // For example, setup a button click listener, etc.
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        /*if (currentUser == null) {
            //  No user is signed in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            //  User is signed in
            //  Here you could go to your home screen or wherever is appropriate
            startActivity(Intent(this, MainScreenActivity::class.java))
            finish()
        }*/
    }

    // Additional methods for handling user interactions can be added here
}
