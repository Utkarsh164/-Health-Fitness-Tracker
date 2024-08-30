package com.example.fitnessapp.pages

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fitnessapp.MusicViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Clear
import com.example.fitnessapp.R

@Composable
fun PlayerPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: MusicViewModel,
    name: String,
    cover: String,
    link: String
) {
    val mediaPlayer = remember { MediaPlayer() }
    val isPlaying = remember { mutableStateOf(false) }
    val sliderPosition = remember { mutableStateOf(0f) }
    val totalDuration = remember { mutableStateOf(0) }
    val isLooping = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(link) {
        mediaPlayer.setDataSource(link)
        mediaPlayer.prepare()
        totalDuration.value = mediaPlayer.duration
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    LaunchedEffect(isPlaying.value) {
        if (isPlaying.value) {
            coroutineScope.launch {
                while (mediaPlayer.isPlaying) {
                    sliderPosition.value = mediaPlayer.currentPosition.toFloat() / totalDuration.value.toFloat()
                    delay(100L) // Update the position every 100 ms
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            //.background(Color(0xFF121212))
            .background(Color(0xFF101D40))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(
                modifier = Modifier
                    //.fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = cover,
                    contentDescription = "Album cover",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = name,
                    fontSize = 24.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                Slider(
                    value = sliderPosition.value,
                    onValueChange = { newValue ->
                        sliderPosition.value = newValue
                        val seekPosition = (newValue * totalDuration.value).toInt()
                        mediaPlayer.seekTo(seekPosition)
                    },
                    modifier = Modifier.fillMaxWidth(0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {
                                if (isPlaying.value) {
                                    mediaPlayer.pause()
                                } else {
                                    mediaPlayer.start()
                                }
                                isPlaying.value = !isPlaying.value
                            }
                        ) {
                            val playPauseIcon: Painter = if (isPlaying.value) {
                                painterResource(id = R.drawable.baseline_pause_24) // Custom pause icon from drawable
                            } else {
                                painterResource(id = R.drawable.baseline_play_arrow_24) // Custom play icon from drawable
                            }
                            Icon(
                                painter = playPauseIcon,
                                contentDescription = if (isPlaying.value) "Pause" else "Play",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = if (isPlaying.value) "Pause" else "Play",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {
                                isLooping.value = !isLooping.value
                                mediaPlayer.isLooping = isLooping.value
                            }
                        ) {
                            val loopIcon: ImageVector = if (isLooping.value) {
                                Icons.Filled.Check
                            } else {
                                Icons.Outlined.Clear
                            }
                            Icon(
                                imageVector = loopIcon,
                                contentDescription = if (isLooping.value) "Disable Loop" else "Enable Loop",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Loop",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
