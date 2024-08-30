package com.example.fitnessapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitnessapp.AuthState
import com.example.fitnessapp.MusicViewModel
import com.example.fitnessapp.Task
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: MusicViewModel,
) {
    val authState = authViewModel.authState.observeAsState()
    val tasks = authViewModel.tasks.observeAsState(listOf())
    var showAddDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf<Pair<String, Task>?>(null) }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("LoginPage")
            else -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1976D2))
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Fitness Tracker",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43D849))
            ) {
                Text(text = "Add New Activity")
            }

            Button(
                onClick = { navController.navigate("ProgressPage")},
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43D849))
            ) {
                Text(text = "View Progress")
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tasks.value) { task ->
                FitnessActivityCard(
                    activityName = task.name,
                    goalDuration = task.goalDuration,
                    actualDuration = task.actualDuration,
                    onUpdateClick = {
                        showUpdateDialog = task.id?.let { id -> id to task }
                    }
                )
            }
        }

        if (showAddDialog) {
            AddTaskDialog(
                onDismiss = { showAddDialog = false },
                onAddTask = { taskName, goalDuration ->
                    authViewModel.addTask(taskName, goalDuration)
                    showAddDialog = false
                }
            )
        }

        showUpdateDialog?.let { (id, task) ->
            UpdateTaskDialog(
                task = task,
                onDismiss = { showUpdateDialog = null },
                onUpdateTask = { actualDuration ->
                    authViewModel.updateTask(id, actualDuration)
                    showUpdateDialog = null
                }
            )
        }
    }
}




@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (String, String) -> Unit // Ensure correct parameters here
) {
    var taskName by remember { mutableStateOf("") }
    var goalDuration by remember { mutableStateOf("") }
    var actualDuration by remember { mutableStateOf("0") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add New Activity") },
        text = {
            Column {
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Task Name") }
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = goalDuration,
                    onValueChange = { goalDuration = it },
                    label = { Text("Goal ") }
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = actualDuration,
                    onValueChange = { actualDuration = it },
                    label = { Text("Today") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onAddTask(taskName, goalDuration) }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}





@Composable
fun FitnessActivityCard(
    activityName: String,
    goalDuration: String,
    actualDuration: String,
    onUpdateClick: () -> Unit // Add this parameter
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
            Button(onClick = onUpdateClick) {
                Text(text = "Update")
            }
        }
    }
}




@Composable
fun UpdateTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onUpdateTask: (String) -> Unit
) {
    var actualDuration by remember { mutableStateOf(task.actualDuration) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Update Activity") },
        text = {
            Column {
                Text("Update the actual duration or distance for the task")
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = actualDuration,
                    onValueChange = { actualDuration = it },
                    label = { Text("Actual Duration") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onUpdateTask(actualDuration) }) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
