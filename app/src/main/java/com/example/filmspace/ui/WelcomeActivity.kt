package com.example.filmspace.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.filmspace.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val signInButton = findViewById<Button>(R.id.buttonSignIn)
        val signUpButton = findViewById<Button>(R.id.buttonSignUp)

        signInButton.setOnClickListener {
            val intent = Intent(this, RegisterLoginActivity::class.java)
            intent.putExtra("fragment", "SignIn")
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, RegisterLoginActivity::class.java)
            intent.putExtra("fragment", "SignUp")
            startActivity(intent)
        }
    }
}