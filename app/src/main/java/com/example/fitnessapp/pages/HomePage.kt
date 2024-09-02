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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitnessapp.AuthState
import com.example.fitnessapp.MusicViewModel
import com.example.fitnessapp.R
import com.example.fitnessapp.Task
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: MusicViewModel,
) {
    val authState by authViewModel.authState.observeAsState()
    val tasks by authViewModel.tasks.observeAsState(listOf())
    var showAddDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf<Pair<String, Task>?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate("LoginPage")
        }
    }

    LaunchedEffect(Unit) {
        // Initial data fetch
        isLoading = true
        authViewModel.fetchTasks()
        isLoading = false
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

        // Buttons for adding new activity and viewing progress
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

            Spacer(modifier = Modifier.width(16.dp)) // Add space between buttons

            Button(
                onClick = { navController.navigate("ProgressPage") },
                modifier = Modifier.width(200.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43D849))
            ) {
                Text(text = "View Progress")
            }
        }

        // Display a reload icon if list is empty
        if (tasks.isEmpty() && !isLoading) {
            IconButton(
                onClick = {
                    isLoading = true
                    coroutineScope.launch {
                        authViewModel.fetchTasks()
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .size(78.dp) // Increase icon size
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_replay_circle_filled_24), // Replace with your reload icon resource
                    contentDescription = "Reload Data",
                    tint = Color.White
                )
            }
        }

        // Display a loading indicator if data is loading
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            // Display tasks
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tasks) { task ->
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
        }

        // Dialogs
        if (showAddDialog) {
            AddTaskDialog(
                onDismiss = {
                    showAddDialog = false
                    authViewModel.fetchTasks() // Refresh the tasks after adding a new one
                },
                onAddTask = { taskName, goalDuration ->
                    authViewModel.addTask(taskName, goalDuration)
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
fun AddTaskDialogs(
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
                    label = { Text("Goal") }
                )
                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = actualDuration,
                    onValueChange = { actualDuration = it },
                    label = { Text("Achieved") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onAddTask(taskName, goalDuration)
                // After adding the task, close the dialog
                onDismiss()
            }) {
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
            Text(text = "Achieved: ${if (actualDuration.isEmpty()) "0" else actualDuration}", fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = onUpdateClick) {
                Text(text = "Update")
            }
        }
    }
}

@Composable
fun FitnessActivityCards(
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
            Text(text = "Achived: $actualDuration", fontSize = 16.sp, color = Color.White)
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
                    label = { Text("Achived") }
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

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (String, String) -> Unit
) {
    var taskName by remember { mutableStateOf("") }
    var goalDuration by remember { mutableStateOf("") }

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
                    label = { Text("Goal") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (taskName.isNotEmpty() && goalDuration.isNotEmpty()) {
                    onAddTask(taskName, goalDuration)
                    onDismiss()
                }
            }) {
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