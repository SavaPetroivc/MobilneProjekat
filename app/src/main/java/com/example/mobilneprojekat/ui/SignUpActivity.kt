package com.example.mobilneprojekat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.UserViewModel

class SignUpActivity : AppCompatActivity() {

    private var viewModel: UserViewModel = UserViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val signUpButton = findViewById<Button>(R.id.signUpButton) as Button

        signUpButton.setOnClickListener{
            val emailInput = findViewById<EditText>(R.id.emailSU)
            val email = emailInput.text.toString()
            val passwordInput = findViewById<EditText>(R.id.passwordSU)
            val password = passwordInput.text.toString()
            val confirmPasswordInput = findViewById<EditText>(R.id.confirmPasswordSU)
            val confirmPassword = confirmPasswordInput.text.toString()

            if(password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if(password == confirmPassword){
                    if(email.isNotEmpty())
                        viewModel.signup(email, password)
                }
            }
        }

        viewModel.userReg.observe(this, Observer { userReg ->
            if(userReg != null){
                val intent = Intent(this, SetUpProfileActivity::class.java)
                intent.putExtra("uid", userReg.uid)

                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,
                    "Authentification failed",
                    Toast.LENGTH_SHORT).show()
            }
        })

        val loginButton = findViewById<TextView>(R.id.loginTransitionButton)

        loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
            finish()
        }
    }
}