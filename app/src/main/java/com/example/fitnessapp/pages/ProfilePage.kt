package com.example.fitnessapp.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    val userData by viewModel.userData.observeAsState()
    val userEmail by viewModel.userEmail.observeAsState()

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    val context = LocalContext.current

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
            // Only update the state variables when userData is non-null
            userData?.let { data ->
                // Update state only when userData changes
                LaunchedEffect(data) {
                    username = data["username"] ?: ""
                    height = data["height"] ?: ""
                    weight = data["weight"] ?: ""
                }

                Text(
                    text = "Username: $username",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Email: ${userEmail ?: "N/A"}",
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text(text = "Height (in cm)") },
                    modifier = Modifier.fillMaxWidth() // Ensure the TextField is properly sized
                )
                Spacer(modifier = Modifier.height(8.dp)) // Use height for better spacing
                TextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text(text = "Weight (in kg)") },
                    modifier = Modifier.fillMaxWidth() // Ensure the TextField is properly sized
                )
            } ?: Text(
                text = "Loading...",
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Button(
            onClick = {
                val heightInFeet = height.toFloatOrNull()
                val weightInKg = weight.toFloatOrNull()
                if (heightInFeet != null && weightInKg != null) {
                    viewModel.updateUserProfile(username, heightInFeet, weightInKg)
                } else {
                    Toast.makeText(context, "Please enter valid numbers for height and weight", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp), // Adjust padding to avoid overlap
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
        ) {
            Text(text = "Update Profile")
        }

        Button(
            onClick = {
                viewModel.logout()
                navController.navigate("LoginPage")
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp), // Adjust padding to avoid overlap
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
        ) {
            Text(text = "Logout")
        }
    }
}
