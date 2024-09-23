package com.example.mobilneprojekat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.UserViewModel

class LoginActivity : AppCompatActivity() {

    private var viewModel: UserViewModel = UserViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton) as Button

        loginButton.setOnClickListener{
            val emailInput = findViewById<EditText>(R.id.emailField)
            val email = emailInput.text.toString()
            val passwordInput = findViewById<EditText>(R.id.passwordField)
            val password = passwordInput.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                viewModel.login(email, password)
            }
        }

        viewModel.userReg.observe(this, Observer { loggedIn ->
            if(loggedIn != null){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("uid", loggedIn.uid)

                startActivity(intent)
                finish()
            } else{
                Toast.makeText(this,
                    "Log In failed",
                    Toast.LENGTH_SHORT).show()
            }
        })

        val signUpButton = findViewById<TextView>(R.id.signupTransitionButton)

        signUpButton.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)

            startActivity(intent)
            finish()
        }
    }
}