package com.example.fitnessapp.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitnessapp.AuthState
import com.example.fitnessapp.MusicViewModel
import com.example.fitnessapp.R

@Composable
fun SignupPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: MusicViewModel) {

    val authen = authViewModel.authState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    val context = LocalContext.current
    LaunchedEffect(authen.value) {
        when (authen.value) {
            is AuthState.Authenticated -> navController.navigate("MainPage")
            is AuthState.Error -> Toast.makeText(context, (authen.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(3.dp))
        Text(text = "Sign-up", fontSize = 53.sp, color = Color(0xFF43D849))
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text(text = "Username") })

            Spacer(modifier = Modifier.padding(4.dp))

            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text(text = "Email") })

            Spacer(modifier = Modifier.padding(4.dp))

            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text(text = "Password") })

            Spacer(modifier = Modifier.padding(4.dp))

            Button(onClick = { authViewModel.signupbro(email, password, username) }) {
                Text(text = "Signup")
            }
        }
    }
}
