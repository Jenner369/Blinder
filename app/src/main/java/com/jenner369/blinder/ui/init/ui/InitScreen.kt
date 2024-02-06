package com.jenner369.blinder.ui.init.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.jenner369.blinder.R
import com.jenner369.blinder.ui.navigator.AppScreens
import com.jenner369.blinder.ui.theme.Grey

@Composable
fun InitScreen(navController: NavController? = null, viewModel: InitViewModel? = null) {
    Box(modifier = Modifier.fillMaxSize()) {
        Init(
            Modifier
                .fillMaxSize()
                .background(color = Color.Black)
                .align(Alignment.Center),
            navController,
            viewModel
        )

    }
}

@Composable
fun Init(modifier: Modifier, navController: NavController? = null, viewModel: InitViewModel? = null) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HeaderImage(modifier = Modifier.weight(2f)
            .drawWithContent {
                val colors = listOf(

                    Color.Transparent,

                    Color.Black,
                )
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(colors, size.height*(.3f), size.height),
                    blendMode = BlendMode.DstOut
                )
            }
        )
        InitContent(modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth(),
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.smart_glasses),
        contentDescription = "Image Header",
        modifier = modifier,
        contentScale = ContentScale.Crop
        )
}

@Composable
fun InitContent(modifier: Modifier, navController: NavController? = null, viewModel: InitViewModel? = null) {
    Box(
        modifier = modifier
    ) {
        Column {
            Text(
                text = "El futuro al frente de tus ojos",
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                lineHeight = 48.sp,
                color = Color.White,
                modifier = Modifier.padding(16.dp, 0.dp),
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = "Tu tienes el control, rompe los límites",
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Grey,
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    var route = AppScreens.NextStepName.route
                    if (viewModel?.checkIfUserExists() == true) {
                        route = AppScreens.Home.route
                    }
                    navController?.navigate(AppScreens.NextStepName.route)
                },
                shape = RoundedCornerShape(30),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    //.padding(16.dp)
                    .fillMaxWidth(.8f),
            ) {
                Text(
                    text = "Empieza ahora",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}