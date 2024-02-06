package com.jenner369.blinder.ui.next_step.permissions.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jenner369.blinder.R
import com.jenner369.blinder.ui.navigator.AppScreens
import com.jenner369.blinder.ui.theme.Grey2

@Composable
fun NextStepPermissionsScreen(navController: NavController? = null, viewModel: NextStepPermissionsViewModel? = null) {
    Box(modifier = Modifier.fillMaxSize()) {
        NextStepPermissions(
            modifier = Modifier.align(Alignment.Center).fillMaxSize(),
            navController = navController,
            viewModel = viewModel)
    }
}

@Composable
fun NextStepPermissions(modifier: Modifier = Modifier, navController: NavController? = null, viewModel: NextStepPermissionsViewModel? = null) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Necesitamos algunos permisos para que todo vaya bien",
            fontSize = 48.sp,
            lineHeight = 48.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(2.dp),
            color = Grey2)
        Spacer(modifier = Modifier.padding(16.dp))
        ButtonNext(modifier = Modifier
            .align(Alignment.CenterHorizontally),
            navController = navController,
            viewModel = viewModel)
        Spacer(modifier = Modifier.padding(32.dp))
        ImageBottom(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .align(Alignment.End))
    }
}

@Composable
fun ButtonNext(modifier: Modifier = Modifier, navController: NavController? = null, viewModel: NextStepPermissionsViewModel? = null) {
    Button(onClick = {
        viewModel?.saveName()
        navController?.navigate(AppScreens.Home.route)
    },
        shape = RoundedCornerShape(30),
        modifier = modifier,
    ) {
        Text(text = "Otorgar")
    }
}


@Composable
fun ImageBottom(modifier: Modifier = Modifier) {
    Image(painter = painterResource(id = R.drawable.next_step_permissions),
        contentDescription = "Next step name image",
        modifier = modifier,
        contentScale = ContentScale.Fit)
}