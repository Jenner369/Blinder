package com.jenner369.blinder.ui.next_step.name.ui

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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.jenner369.blinder.R
import com.jenner369.blinder.ui.navigator.AppScreens
import com.jenner369.blinder.ui.theme.Grey2

@Composable
fun NextStepNameScreen(navController: NavController? = null, viewModel: NextStepNameViewModel? = null) {
    Box(modifier = Modifier.fillMaxSize()) {
        NextStepName(
            modifier = Modifier.align(Alignment.Center).fillMaxSize(),
            navController = navController,
            viewModel = viewModel)
    }
}

@Composable
fun NextStepName(modifier: Modifier = Modifier, navController: NavController? = null, viewModel: NextStepNameViewModel? = null) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Antes que nada, queremos saber como te llamas?",
            fontSize = 48.sp,
            lineHeight = 48.sp,
            textAlign = TextAlign.Center,
            color = Grey2)
        Spacer(modifier = Modifier.padding(16.dp))
        InputName(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
            viewModel = viewModel)
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
fun ButtonNext(modifier: Modifier = Modifier, navController: NavController? = null, viewModel: NextStepNameViewModel? = null) {
    val enableNext = viewModel?.enableNext?.observeAsState(initial = false)
    Button(onClick = {
        viewModel?.saveName()
        navController?.navigate(AppScreens.NextStepPermissions.route)
    },
        shape = RoundedCornerShape(30),
        modifier = modifier,
        enabled = enableNext!!.value,
    ) {
        Text(text = "Siguiente")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputName(modifier: Modifier = Modifier, viewModel: NextStepNameViewModel? = null) {
    val name = viewModel?.name?.observeAsState(initial = viewModel.initName())

    TextField(value = name!!.value, onValueChange = { viewModel.onNameChanged(it) },
        maxLines = 1,
        placeholder = { Text(text = "Escribe tu nombre") },
        )
}

@Composable
fun ImageBottom(modifier: Modifier = Modifier) {
    Image(painter = painterResource(id = R.drawable.next_step_name),
        contentDescription = "Next step name image",
        modifier = modifier,
        contentScale = ContentScale.Fit)
}