package com.example.graduationproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.graduationproject.database.DatabaseManager
import com.example.graduationproject.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var databaseManager: DatabaseManager
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        databaseManager = DatabaseManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.signup) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            signUpButton.setOnClickListener {
                val fullName = editTextFullName.text.toString().trim()
                val email = editTextEmail.text.toString().trim()
                val password = editTextPassword.text.toString().trim()

                if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    val userId = databaseManager.insertUser(fullName, email, password)
                    if (userId > -1) {
                        Toast.makeText(this@SignupActivity, "User registered successfully!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@SignupActivity, "Error registering user!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@SignupActivity, "Please fill all the fields", Toast.LENGTH_LONG).show()
                }
            }

            redirectToLogin.setOnClickListener {
                // Handle redirection to login
                // For now, just show a Toast message
                Toast.makeText(this@SignupActivity, "Redirected to login", Toast.LENGTH_LONG).show()
                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}