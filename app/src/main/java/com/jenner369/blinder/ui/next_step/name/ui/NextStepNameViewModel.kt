package com.jenner369.blinder.ui.next_step.name.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jenner369.blinder.database.mySQLiteHelper

class NextStepNameViewModel(private val userDbSQLiteHelper: mySQLiteHelper) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name

    private val _enableNext = MutableLiveData<Boolean>()
    val enableNext : LiveData<Boolean> = _enableNext

    init {
        var nameStr = ""
        if (userDbSQLiteHelper.checkIfUserExists()) {
            nameStr = userDbSQLiteHelper.getUniqueUser()
        }
        _name.value = nameStr
        _enableNext.value = nameStr.isNotEmpty()
    }

    fun initName(): String {
        var nameStr = ""
        if (userDbSQLiteHelper.checkIfUserExists()) {
            nameStr = userDbSQLiteHelper.getUniqueUser()
        }
        return nameStr
    }

    fun onNameChanged(name: String) {
        _name.value = name
        _enableNext.value = name.isNotEmpty()
    }

    fun saveName() {
        userDbSQLiteHelper.deleteAllUsers()
        userDbSQLiteHelper.addUser(name.value!!)
    }
}