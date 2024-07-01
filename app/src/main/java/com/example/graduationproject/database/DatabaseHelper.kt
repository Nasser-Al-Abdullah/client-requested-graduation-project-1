package com.example.graduationproject.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "graduation_project.db"
        const val DATABASE_VERSION = 1

        const val TABLE_USER = "user"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USER_FULL_NAME = "full_name"
        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_USER_PASSWORD = "password"

        const val TABLE_GRADE = "grade"
        const val COLUMN_GRADE_ID = "grade_id"
        const val COLUMN_GRADE_STUDENT_ID = "student_id"
        const val COLUMN_GRADE_STUDENT_NAME = "student_name"
        const val COLUMN_GRADE_COURSE_ID = "course_id"
        const val COLUMN_GRADE_COURSE_NAME = "course_name"
        const val COLUMN_GRADE_GRADE = "grade"
        const val COLUMN_GRADE_USER_ID = "user_id"

        const val CREATE_TABLE_USER = (
                "CREATE TABLE $TABLE_USER (" +
                        "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_USER_FULL_NAME TEXT NOT NULL, " +
                        "$COLUMN_USER_EMAIL TEXT NOT NULL, " +
                        "$COLUMN_USER_PASSWORD TEXT NOT NULL)"
                )

        const val CREATE_TABLE_GRADE = (
                "CREATE TABLE $TABLE_GRADE (" +
                        "$COLUMN_GRADE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_GRADE_STUDENT_ID TEXT NOT NULL, " +
                        "$COLUMN_GRADE_STUDENT_NAME TEXT NOT NULL, " +
                        "$COLUMN_GRADE_COURSE_ID TEXT NOT NULL, " +
                        "$COLUMN_GRADE_COURSE_NAME TEXT NOT NULL, " +
                        "$COLUMN_GRADE_GRADE TEXT NOT NULL, " +
                        "$COLUMN_GRADE_USER_ID INTEGER, " +
                        "FOREIGN KEY($COLUMN_GRADE_USER_ID) REFERENCES $TABLE_USER($COLUMN_USER_ID))"
                )
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_USER)
        db.execSQL(CREATE_TABLE_GRADE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GRADE")
        onCreate(db)
    }
}
