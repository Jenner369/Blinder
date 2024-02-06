package com.jenner369.blinder.ui.navigator

sealed class AppScreens(val route: String) {
    object Init: AppScreens("init")
    object Home: AppScreens("home")
    object NextStepName: AppScreens("nextStepName")
    object NextStepPermissions: AppScreens("nextStepPermissions")
}
