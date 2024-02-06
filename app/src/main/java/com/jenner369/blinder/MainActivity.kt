package com.jenner369.blinder

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.media.browse.MediaBrowser.MediaItem
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jenner369.blinder.database.mySQLiteHelper
import com.jenner369.blinder.ocr.Assets
import com.jenner369.blinder.ui.init.ui.InitScreen
import com.jenner369.blinder.ui.navigator.AppNavigation
import com.jenner369.blinder.ui.theme.BlinderTheme
import io.ktor.application.Application
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class MainActivity : ComponentActivity() {
    private lateinit var userDbHelper: SQLiteOpenHelper
    private lateinit var tts: TextToSpeech

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDbHelper = mySQLiteHelper(this)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(java.util.Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "This language is not supported", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dataPath = this.filesDir.absolutePath

        Assets.extractAssets(this)
        setContent {
            BlinderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavigation(userDbHelper = userDbHelper as mySQLiteHelper,
                        tts = tts)
                }
            }
        }
    }
    companion object {
        lateinit var dataPath : String
    }

}