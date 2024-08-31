package com.example.fitnessapp.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun SignupPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: MusicViewModel) {

    val authen = authViewModel.authState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val context = LocalContext.current
    LaunchedEffect(authen.value) {
        when (authen.value) {
            is AuthState.Authenticated -> navController.navigate("MainPage")
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

        // Title Text
        Text(
            text = "Sign-up",
            fontSize = 53.sp,
            color = Color.White
        )

        // Logo Image
        val logo: Painter = painterResource(id = R.drawable.logo) // Replace with your logo resource ID
        Image(
            painter = logo,
            contentDescription = "Logo",
            modifier = Modifier
                .padding(16.dp)
                .size(100.dp) // Adjust size as needed
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(value = username, onValueChange = { username = it }, label = { Text(text = "Username") })

            Spacer(modifier = Modifier.padding(4.dp))

            TextField(value = email, onValueChange = { email = it }, label = { Text(text = "Email") })

            Spacer(modifier = Modifier.padding(4.dp))

            TextField(value = password, onValueChange = { password = it }, label = { Text(text = "Password") })

            Spacer(modifier = Modifier.padding(4.dp))

            TextField(value = height, onValueChange = { height = it }, label = { Text(text = "Height (in cm)") }) // Updated label

            Spacer(modifier = Modifier.padding(4.dp))

            TextField(value = weight, onValueChange = { weight = it }, label = { Text(text = "Weight (in kg)") })

            Spacer(modifier = Modifier.padding(4.dp))

            Button(onClick = {
                val heightInCm = height.toFloatOrNull()
                val weightInKg = weight.toFloatOrNull()
                if (heightInCm != null && weightInKg != null) {
                    authViewModel.signupbro(email, password, username, heightInCm, weightInKg)
                } else {
                    Toast.makeText(context, "Please enter valid numbers for height and weight", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Signup")
            }
        }
    }
}
