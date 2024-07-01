package com.example.graduationproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.graduationproject.database.DatabaseManager
import com.example.graduationproject.databinding.ActivityLoginBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var dbManager: DatabaseManager
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)  // Updated to use the root view from binding

        dbManager = DatabaseManager(this)
        dataStoreManager = DataStoreManager(this)

        // Check if the user is already logged in
        lifecycleScope.launch {
            val keepSignedIn = dataStoreManager.keepSignedIn.first()
            if (keepSignedIn) {
                val email = dataStoreManager.userEmail.first()
                val password = dataStoreManager.userPassword.first()
                if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    val userId = dbManager.checkUserCredentials(email, password)
                    if (userId != -1L) {
                        navigateToHome()
                    }
                }
            }
        }

        binding.button.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val keepSignedIn = binding.checkBoxKeepSignedIn.isChecked

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val userId = dbManager.checkUserCredentials(email, password)
                if (userId != -1L) {
                    lifecycleScope.launch {
                        dataStoreManager.saveUserCredentials(email, password, keepSignedIn)
                    }
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.redirectToSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
