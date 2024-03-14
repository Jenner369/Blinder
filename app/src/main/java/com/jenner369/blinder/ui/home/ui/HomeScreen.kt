package com.jenner369.blinder.ui.home.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jenner369.blinder.MainActivity
import com.jenner369.blinder.ui.navigator.AppScreens
import com.jenner369.blinder.ui.next_step.permissions.ui.NextStepPermissions
import com.jenner369.blinder.ui.next_step.permissions.ui.NextStepPermissionsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController? = null, viewModel: HomeViewModel? = null) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Blinder Server",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel?.onDestroy()
                        navController?.navigate(AppScreens.Init.route)
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel?.cleanTextRecognized()
            }) {
                Text(text = "Clean")
            }
        }
    ) {
        HomeContent(it = it, navController = navController, viewModel = viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeContent(it: PaddingValues, navController: NavController? = null, viewModel: HomeViewModel? = null) {
    val isRunning = viewModel?.isRunning?.observeAsState(false)
    val textRecognized = viewModel?.textRecognized?.observeAsState("")
    val bitmap = viewModel?.bitmap?.observeAsState(null)
    // Creates a CoroutineScope bound to the MainScreen lifecycle
    val scope = rememberCoroutineScope()
    val selectedOption = viewModel?.selectedOption?.observeAsState("GCS")
    var loading by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Text(text = "Bienvenido, ${viewModel?.getCurrentName()}", color = Color.Black, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = {
            loading = true
             scope.launch {
                    delay(100)
                    viewModel?.startServer()
                    loading = false
             }
        },
        enabled = isRunning?.value == false && !loading
        ) {
            Text(text = "Iniciar Servidor")
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = {
            viewModel?.onDestroy()
        },
        enabled = isRunning!!.value
        ) {
            Text(text = "Parar Servidor")
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = {
            viewModel?.onDestroy()
            navController?.navigate(AppScreens.NextStepName.route)
        }) {
            Text(text = "Cambiar nombre")
        }

        if (!isRunning!!.value) {
            Spacer(modifier = Modifier.padding(8.dp))
            Row {
                RadioButton(
                    selected = selectedOption!!.value == "GCS",
                    onClick = { viewModel.changeOptionSelected("GCS") }
                )
                Text("Google Cloud Vision (Requiere Internet, más preciso pero lento)")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row {
                RadioButton(
                    selected = selectedOption!!.value == "MLG",
                    onClick = { viewModel.changeOptionSelected("MLG") }
                )
                Text("ML Google Kit v2 (No requiere de internet, más rápido, pero menos preciso)")
            }
        }

        if (isRunning!!.value) {
            Text(text = "Servidor corriendo", color = Color.Green)
        }
        if (bitmap?.value != null) {
            Image(
                bitmap = bitmap.value!!.asImageBitmap(),
                contentDescription = "Image",
                modifier = Modifier
                    //Set width of bitmap
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        ContentColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(
                rememberScrollState()
            )) {
            Text(text = textRecognized!!.value)
        }
    }
}

@Composable
fun ContentColumn(modifier: Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
    ) {
        content()
    }
}