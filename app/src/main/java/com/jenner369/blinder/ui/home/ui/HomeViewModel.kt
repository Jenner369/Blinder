package com.jenner369.blinder.ui.home.ui

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jenner369.blinder.database.mySQLiteHelper
import com.jenner369.blinder.module
import com.jenner369.blinder.ocr.OcrHandler
import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive

class HomeViewModel(
    private val userDbSQLiteHelper: mySQLiteHelper,
    private val tts: TextToSpeech
) : ViewModel() {

    private lateinit var server: NettyApplicationEngine


    val _textRecognized = MutableLiveData<String>()
    val textRecognized: LiveData<String> = _textRecognized

    private val _isRunning = MutableLiveData<Boolean>()
    val isRunning: LiveData<Boolean> = _isRunning


    fun getCurrentName(): String {
        return userDbSQLiteHelper.getUniqueUser()
    }

    fun cleanTextRecognized() {
        stopSpeech()
        _textRecognized.value = ""
    }

    fun onRecognizedTextChanged(text: String) {
        _textRecognized.postValue(text)
        tts.speak(text.trim(), TextToSpeech.QUEUE_FLUSH, null, null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startServer() {
        _isRunning.value = true
        try {
            stopSpeech()
            server = embeddedServer(Netty, port = 8080, module = Application::module)
            server.start()
            OcrHandler.instance?._textRecognized?.value = "Esperando por traduccion"
        }
        catch (e: Exception) {
            _isRunning.value = false
        }
    }

    fun onDestroy() {
        if (_isRunning.value == true) {
            server.stop(0, 0)
            _isRunning.value = false
        }
        cleanTextRecognized()
    }

    private fun stopSpeech(){
        tts.stop()
    }
}