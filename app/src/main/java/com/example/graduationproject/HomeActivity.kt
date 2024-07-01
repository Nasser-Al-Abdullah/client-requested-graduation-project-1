package com.example.graduationproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.graduationproject.database.DatabaseManager
import com.example.graduationproject.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var dbManager: DatabaseManager
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)  // Updated to use the root view from binding

        dbManager = DatabaseManager(this)
        dataStoreManager = DataStoreManager(this)

        binding.button.setOnClickListener {
            val studentName = binding.editTextStudentName.text.toString().trim()
            val studentId = binding.editTextStudentId.text.toString().trim()
            val courseName = binding.editTextCourseName.text.toString().trim()
            val courseId = binding.editTextCourseId.text.toString().trim()
            val grade = binding.editTextGrade.text.toString().trim()

            if (studentName.isNotEmpty() && studentId.isNotEmpty() &&
                courseName.isNotEmpty() && courseId.isNotEmpty() && grade.isNotEmpty()) {

                val userId = 1L // Replace with logic to get user ID from session or intent
                val gradeId = dbManager.insertGrade(studentId, studentName, courseId, courseName, grade, userId)

                if (gradeId > 0) {
                    Toast.makeText(this, "Grade added successfully!", Toast.LENGTH_SHORT).show()
                    // Handle success, maybe navigate to another activity
                } else {
                    Toast.makeText(this, "Failed to add grade. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSignOut.setOnClickListener {
            lifecycleScope.launch {
                dataStoreManager.clearUserCredentials()
                Toast.makeText(this@HomeActivity, "Signed out successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.checkgrades.setOnClickListener {
            val intent = Intent(this@HomeActivity, GradesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
