package com.example.misegundaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.misegundaapp.R
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var signUpButton: Button
    lateinit var loginOption: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Assign the inputs to the variables
        emailInput = findViewById(R.id.etEmail)
        passwordInput = findViewById(R.id.etPassword)
        signUpButton = findViewById(R.id.btnSignup)
        loginOption = findViewById(R.id.link_login_option)

        // Set onClickListener for the sign-up button
        signUpButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                showError("Please fill in both email and password.")
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                showError("Invalid email format.")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showError("Password should be at least 6 characters.")
                return@setOnClickListener
            }

            // Register the user using Firebase
            signUp(email, password)
        }

        // Set onClickListener for Sign Up link to navigate to SignUpActivity
        loginOption.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // Function to show error message in a notification banner
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Function to register the user using Firebase
    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showError("Sign up Successfull.")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showError("Sign up failed.")
                }
            }
    }
}