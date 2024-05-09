package com.example.swcapstone

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호는 비워둘 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(baseContext, "암호의 길이는 6자 이상이어야 합니다!", Toast.LENGTH_SHORT).show()
            return // Stop further execution
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Register", "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    // Update UI or go to another Activity
                    Toast.makeText(this@RegisterActivity, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Register", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this@RegisterActivity, "회원가입에 실패 하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}