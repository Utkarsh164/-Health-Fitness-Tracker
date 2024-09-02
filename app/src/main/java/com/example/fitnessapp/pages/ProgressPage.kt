package com.example.fitnessapp.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitnessapp.MusicViewModel
import java.util.Calendar

@Composable
fun ProgressPage(
    navController: NavController,
    authViewModel: MusicViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf<String?>(null) } // Used to trigger the toast

    val tasks = authViewModel.tasks.observeAsState(listOf())
    val context = LocalContext.current
    val userData = authViewModel.userData.observeAsState(emptyMap())

    // Show the toast message when `showToast` is not null
    showToast?.let {
        LaunchedEffect(it) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            showToast = null // Reset after showing
        }
    }

    // Calculate BMI
    val heightCm = userData.value["height"]?.toFloatOrNull() ?: 0f
    val weightKg = userData.value["weight"]?.toFloatOrNull() ?: 0f
    val heightMeters = heightCm / 100f // Convert cm to meters
    val bmi = if (heightMeters > 0) weightKg / (heightMeters * heightMeters) else 0f

    // Determine BMI category
    val bmiCategory = when {
        bmi < 18.5 -> "Underweight"
        bmi in 18.5..24.9 -> "Normal"
        bmi in 25.0..29.9 -> "Overweight"
        else -> "Obese"
    }

    // Determine color based on BMI category
    val bmiColor = when (bmiCategory) {
        "Normal" -> Color(0xFF43D849)
        else -> Color.Red
    }

    Column(
        modifier = Modifier.background(Color(0xFF1976D2))
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center-align horizontally
    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "View Progress",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .padding(bottom = 24.dp) // Add bottom padding
        )

        // Display BMI
        Text(
            text = "BMI: %.2f ($bmiCategory)".format(bmi),
            fontSize = 20.sp,
            color = bmiColor,
            modifier = Modifier
                .padding(bottom = 16.dp) // Add padding above the OutlinedTextField
        )

        TextField(
            value = day,
            onValueChange = { day = it },
            label = { Text("Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(
            value = month,
            onValueChange = { month = it },
            label = { Text("Month") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                val dayInt = day.toIntOrNull()
                val monthInt = month.toIntOrNull()
                val yearInt = year.toIntOrNull()

                if (dayInt == null || dayInt !in 1..31) {
                    showToast = "Invalid day"
                } else if (monthInt == null || monthInt !in 1..12) {
                    showToast = "Invalid month"
                } else if (yearInt == null || yearInt < 1900 || yearInt > Calendar.getInstance().get(Calendar.YEAR)) {
                    showToast = "Invalid year"
                } else {
                    authViewModel.fetchTasksForDate(dayInt, monthInt, yearInt)
                }
                keyboardController?.hide()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Show Progress")
        }


        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tasks.value) { task ->
                FitnessActivity(
                    activityName = task.name,
                    goalDuration = task.goalDuration,
                    actualDuration = task.actualDuration
                )
            }
        }
    }
}

@Composable
fun FitnessActivity(
    activityName: String,
    goalDuration: String,
    actualDuration: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF101D40))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = activityName, fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Goal: $goalDuration", fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Achieved: $actualDuration", fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}
