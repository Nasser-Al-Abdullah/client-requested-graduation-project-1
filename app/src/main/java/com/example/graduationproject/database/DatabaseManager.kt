package com.example.graduationproject.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.graduationproject.data.Course
import com.example.graduationproject.data.Student
import com.example.graduationproject.database.DatabaseHelper.Companion.COLUMN_GRADE_COURSE_ID
import com.example.graduationproject.database.DatabaseHelper.Companion.COLUMN_GRADE_COURSE_NAME
import com.example.graduationproject.database.DatabaseHelper.Companion.COLUMN_GRADE_GRADE
import com.example.graduationproject.database.DatabaseHelper.Companion.COLUMN_GRADE_STUDENT_ID
import com.example.graduationproject.database.DatabaseHelper.Companion.COLUMN_GRADE_STUDENT_NAME
import com.example.graduationproject.database.DatabaseHelper.Companion.COLUMN_GRADE_USER_ID
import com.example.graduationproject.database.DatabaseHelper.Companion.COLUMN_USER_EMAIL
import com.example.graduationproject.database.DatabaseHelper.Companion.COLUMN_USER_FULL_NAME
import com.example.graduationproject.database.DatabaseHelper.Companion.COLUMN_USER_PASSWORD
import com.example.graduationproject.database.DatabaseHelper.Companion.TABLE_GRADE
import com.example.graduationproject.database.DatabaseHelper.Companion.TABLE_USER

class DatabaseManager(context: Context) {

    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    fun insertUser(fullName: String, email: String, password: String): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_FULL_NAME, fullName)
            put(COLUMN_USER_EMAIL, email)
            put(COLUMN_USER_PASSWORD, password)
        }
        return db.insert(TABLE_USER, null, values)
    }

    fun checkUserCredentials(email: String, password: String): Long {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val columns = arrayOf(DatabaseHelper.COLUMN_USER_ID)
        val selection = "$COLUMN_USER_EMAIL = ? AND $COLUMN_USER_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)

        var userId: Long = -1

        val cursor: Cursor? = db.query(
            TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(DatabaseHelper.COLUMN_USER_ID)
                if (columnIndex != -1) {
                    userId = it.getLong(columnIndex)
                }
            }
        }

        cursor?.close()
        return userId
    }


    fun insertGrade(studentId: String, studentName: String, courseId: String, courseName: String, grade: String, userId: Long): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_GRADE_STUDENT_ID, studentId)
            put(COLUMN_GRADE_STUDENT_NAME, studentName)
            put(COLUMN_GRADE_COURSE_ID, courseId)
            put(COLUMN_GRADE_COURSE_NAME, courseName)
            put(COLUMN_GRADE_GRADE, grade)
            put(COLUMN_GRADE_USER_ID, userId)
        }
        return db.insert(TABLE_GRADE, null, values)
    }

    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_GRADE} ORDER BY ${DatabaseHelper.COLUMN_GRADE_STUDENT_ID}"

        db.rawQuery(query, null).use { cursor ->
            var currentStudentId: String? = null
            var currentStudentName: String? = null
            var courses = mutableListOf<Course>()

            while (cursor.moveToNext()) {
                val studentIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_GRADE_STUDENT_ID)
                val studentNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_GRADE_STUDENT_NAME)

                // Check if columns exist
                if (studentIdIndex == -1 || studentNameIndex == -1) {
                    // Handle error or skip this row
                    continue
                }

                val studentId = cursor.getString(studentIdIndex)
                val studentName = cursor.getString(studentNameIndex)

                if (studentId != currentStudentId) {
                    // New student encountered, save previous student data
                    if (currentStudentId != null) {
                        students.add(Student(currentStudentId, currentStudentName ?: "", courses.toList()))
                    }

                    // Reset for new student
                    currentStudentId = studentId
                    currentStudentName = studentName
                    courses = mutableListOf()
                }

                // Retrieve course details
                val courseIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_GRADE_COURSE_ID)
                val courseNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_GRADE_COURSE_NAME)
                val gradeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_GRADE_GRADE)

                // Check if columns exist
                if (courseIdIndex == -1 || courseNameIndex == -1 || gradeIndex == -1) {
                    // Handle error or skip this row
                    continue
                }

                val courseId = cursor.getString(courseIdIndex)
                val courseName = cursor.getString(courseNameIndex)
                val grade = cursor.getString(gradeIndex)

                courses.add(Course(courseId, courseName, grade))
            }

            // Add the last student to the list
            if (currentStudentId != null) {
                students.add(Student(currentStudentId, currentStudentName ?: "", courses.toList()))
            }
        }

        return students
    }


}
