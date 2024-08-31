package com.example.fitnessapp.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitnessapp.AuthState
import com.example.fitnessapp.MusicViewModel
import com.example.fitnessapp.R

@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: MusicViewModel) {
    val authen = authViewModel.authState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    LaunchedEffect(authen.value) {
        when (authen.value) {
            is AuthState.Authenticated -> navController.navigate("MainPage") {
                popUpTo("LoginPage") { inclusive = true }
                launchSingleTop = true
            }
            is AuthState.Error -> Toast.makeText(context, (authen.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1976D2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = "Login",
            fontSize = 53.sp,
            color = Color.White
        )

        // Logo Image
        val logo: Painter = painterResource(id = R.drawable.logo) // Replace with your logo resource ID
        Image(painter = logo, contentDescription = "Logo", modifier = Modifier.padding(16.dp))

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Button(onClick = { authViewModel.loginbro(email, password) }) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.padding(16.dp))

            TextButton(onClick = { navController.navigate("SignupPage") }) {
                Text(text = "Create Account?", color = Color(0xFF43D849))
            }
        }
    }
}
