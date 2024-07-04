package com.example.graduationproject

import android.content.Intent
import android.os.Bundle
import com.example.graduationproject.database.DatabaseManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.data.Student
import com.example.graduationproject.databinding.ActivityGradesBinding
import com.example.graduationproject.utility.StudentAdapter

class GradesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGradesBinding
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var students: List<Student>
    private lateinit var dbManager: DatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Initialize DatabaseManager
        dbManager = DatabaseManager(this)
        // Initialize RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize and set adapter (can be empty for now)
        studentAdapter = StudentAdapter(emptyList())
        binding.recyclerView.adapter = studentAdapter


        // Fetch students from database
        fetchStudentsFromDatabase()

        binding.buttonSignOut.setOnClickListener {
            val intent = Intent(this@GradesActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchStudentsFromDatabase() {
        students = dbManager.getAllStudents()
        studentAdapter.updateStudents(students)
    }
}
