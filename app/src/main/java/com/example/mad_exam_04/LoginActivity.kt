package com.example.mad_exam_04

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userPreferences = UserPreferences(this)

        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        // Get pre-filled data from RegisterActivity (if available)
        val prefilledUsername = intent.getStringExtra("username")
        val prefilledPassword = intent.getStringExtra("password")

        usernameEditText.setText(prefilledUsername)
        passwordEditText.setText(prefilledPassword)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val savedUser = userPreferences.getUser()

            if (savedUser != null && savedUser.username == username && savedUser.password == password) {
                // Successful login, navigate to AllTasksActivity
                val intent = Intent(this, AllTasksActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
