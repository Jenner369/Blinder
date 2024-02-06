package com.jenner369.blinder.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class mySQLiteHelper(context: Context) : SQLiteOpenHelper(
    context,
    "blinder.db",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val userTableCreateSQL = "CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
        db?.execSQL(userTableCreateSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val userTableDropSQL = "DROP TABLE IF EXISTS user"
        db?.execSQL(userTableDropSQL)
        onCreate(db)
    }

    fun addUser(name: String) {
        val db = writableDatabase
        val insertSQL = "INSERT INTO user (name) VALUES ('$name')"
        db.execSQL(insertSQL)
        db.close()
    }

    fun deleteAllUsers() {
        val db = writableDatabase
        val deleteSQL = "DELETE FROM user"
        db.execSQL(deleteSQL)
        db.close()
    }

    fun checkIfUserExists(): Boolean {
        val db = readableDatabase
        val querySQL = "SELECT * FROM user"
        val cursor = db.rawQuery(querySQL, null)
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    fun getUniqueUser(): String {
        val db = readableDatabase
        val querySQL = "SELECT * FROM user"
        val cursor = db.rawQuery(querySQL, null)
        cursor.moveToFirst()
        val result = cursor.getString(1)
        cursor.close()
        db.close()
        return result
    }

}