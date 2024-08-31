package com.example.fitnessapp.pages

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fitnessapp.API.Data
import com.example.fitnessapp.API.NetworkResponse
import com.example.fitnessapp.API.MyData
import com.example.fitnessapp.AuthState
import com.example.fitnessapp.MusicViewModel
import com.example.fitnessapp.R

@Composable
fun MusicPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: MusicViewModel,
) {
    val authen by authViewModel.authState.observeAsState()

    LaunchedEffect(authen) {
        if (authen is AuthState.Unauthenticated) {
            navController.navigate("LoginPage")
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1976D2)) // Ensure consistency with color
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Music",
                    fontSize = 35.sp,
                    color = Color.White
                )
            }
        }

        val musicResult by authViewModel.musicResult.observeAsState()
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 0.dp, 10.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LaunchedEffect(Unit) {
                authViewModel.getMusic()
            }

            when (val result = musicResult) {
                is NetworkResponse.Error -> {
                    Spacer(modifier = Modifier.size(200.dp))
                    Text(text = result.message, fontSize = 40.sp)
                }

                NetworkResponse.Loading -> {
                    Spacer(modifier = Modifier.size(250.dp))
                    CircularProgressIndicator()
                }

                is NetworkResponse.Success -> {
                    keyboardController?.hide()
                    MusicDetails(data = result.data, modifier, navController)
                }

                null -> {}
            }
        }
    }
}

@Composable
fun MusicDetails(
    data: MyData,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp) // Consistent padding with HomePage
    ) {
        items(data.data) { item ->
            MusicItem(
                data = item,
                modifier = modifier,
                onClick = {
                    navController.navigate(
                        "PlayerPage/${Uri.encode(item.album.cover)}/${Uri.encode(item.title)}/${Uri.encode(item.preview)}"
                    )
                }
            )
        }
    }
}

@Composable
fun MusicItem(
    data: Data,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp) // Reduced padding
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp) // Padding inside the card
        ) {
            AsyncImage(
                model = data.album.cover_medium,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.Gray, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = data.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = data.artist.name,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = formatDuration(data.duration),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

fun formatDuration(durationInSeconds: Int): String {
    val minutes = durationInSeconds / 60
    val seconds = durationInSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
