package com.example.fitnessapp.pages

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitnessapp.MusicViewModel


@Composable
fun ProgressPage(
    navController: NavController,
    authViewModel: MusicViewModel
) {
    val tasks = authViewModel.tasks.observeAsState(listOf())
    var selectedDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "View Progress", fontSize = 36.sp, fontWeight = FontWeight.Bold)
        // Add UI for selecting date
        // For example, a list of dates or a date picker

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
                .wrapContentHeight(), // Adjust to fit the content
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = activityName, fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Goal: $goalDuration", fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Actual: $actualDuration", fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.size(16.dp))

        }
    }
}




