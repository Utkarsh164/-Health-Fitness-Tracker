package com.example.fitnessapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitnessapp.MusicViewModel
import com.example.fitnessapp.R

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    viewModel: MusicViewModel,
    navController: NavController
) {
    val userData = viewModel.userData.observeAsState()
    val userEmail = viewModel.userEmail.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserData()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1976D2))
            .padding(16.dp)
    ) {

        Text(
            text = "Profile",
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )


        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Profile Logo",
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            userData.value?.let { data ->
                Text(
                    text = "Username: ${data["username"] ?: "N/A"}",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)    )
                Text(
                    text = "Email: ${userEmail.value ?: "N/A"}",
                    fontSize = 20.sp,
                    color = Color.White
                )
            } ?: Text(
                text = "Loading...",
                fontSize = 20.sp,
                color = Color.White
            )
        }


        Button(
            onClick = {
                viewModel.logout()
                navController.navigate("LoginPage")
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))

        ) {
            Text(text = "Logout")
        }
    }
}
