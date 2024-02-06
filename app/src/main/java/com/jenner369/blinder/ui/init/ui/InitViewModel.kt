package com.jenner369.blinder.ui.init.ui

import androidx.lifecycle.ViewModel
import com.jenner369.blinder.database.mySQLiteHelper

class InitViewModel(private val userDbSQLiteHelper: mySQLiteHelper) : ViewModel() {

    fun checkIfUserExists(): Boolean {
        return userDbSQLiteHelper.checkIfUserExists()
    }
}