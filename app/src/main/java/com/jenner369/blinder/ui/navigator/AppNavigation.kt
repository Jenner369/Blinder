package com.jenner369.blinder.ui.navigator

import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jenner369.blinder.database.mySQLiteHelper
import com.jenner369.blinder.ocr.OcrHandler
import com.jenner369.blinder.ui.home.ui.HomeScreen
import com.jenner369.blinder.ui.home.ui.HomeViewModel
import com.jenner369.blinder.ui.init.ui.InitScreen
import com.jenner369.blinder.ui.init.ui.InitViewModel
import com.jenner369.blinder.ui.next_step.name.ui.NextStepNameScreen
import com.jenner369.blinder.ui.next_step.name.ui.NextStepNameViewModel
import com.jenner369.blinder.ui.next_step.permissions.ui.NextStepPermissionsScreen
import com.jenner369.blinder.ui.next_step.permissions.ui.NextStepPermissionsViewModel
import io.ktor.server.netty.NettyApplicationEngine

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(userDbHelper: mySQLiteHelper, tts: TextToSpeech) {
    val navController = rememberNavController()
    var startDestination = AppScreens.Init.route
    if (userDbHelper.checkIfUserExists()) {
        startDestination = AppScreens.Home.route
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = AppScreens.Init.route) {
            InitScreen(navController = navController, viewModel = InitViewModel(userDbHelper))
        }
        composable(route = AppScreens.NextStepName.route) {
            NextStepNameScreen(navController = navController, viewModel = NextStepNameViewModel(userDbHelper))
        }
        composable(route = AppScreens.NextStepPermissions.route) {
            NextStepPermissionsScreen(navController = navController, viewModel = NextStepPermissionsViewModel(userDbHelper))
        }
        composable(route = AppScreens.Home.route) {
            var viewModelInstance = HomeViewModel(userDbHelper, tts)
            OcrHandler.instance = viewModelInstance
            HomeScreen(navController = navController, viewModel = viewModelInstance)
        }
    }
}