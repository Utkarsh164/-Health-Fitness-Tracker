package com.example.fitnessapp.pages

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fitnessapp.API.NetworkResponse
import com.example.fitnessapp.API.MyData
import com.example.fitnessapp.AuthState
import com.example.fitnessapp.MusicViewModel
import com.example.fitnessapp.R

@Composable
fun MusicPage(modifier: Modifier = Modifier,
             navController: NavController,
             authViewModel: MusicViewModel,

             ) {
    val authen = authViewModel.authState.observeAsState()

    LaunchedEffect(authen.value) {
        when (authen.value) {
            is AuthState.Unauthenticated -> navController.navigate("LoginPage")
            else -> Unit
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
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
                    color = Color(0xFF43D849)
                )
            }



        }


        val musicResult = authViewModel.musicResult.observeAsState()
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

            when (val result = musicResult.value) {
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
                    MusicDetails(data = result.data, modifier,navController)
                }

                null -> {}
            }
        }
    }
}

@Composable
fun MusicDetails(data: MyData, modifier: Modifier = Modifier,navController: NavController) {
    LazyColumn {
        items(data.data) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .height(120.dp)
                    .clickable {
                        //navController.navigate("PlayerPage" + item.album.cover + "," + item.title + "," + item.artist.link)
                        navController.navigate("PlayerPage/${Uri.encode(item.album.cover)}/${Uri.encode(item.title)}/${Uri.encode(item.preview)}")

                        //PlayerPage(name = item.title, cover = item.album.cover, link = item.link)


                    },
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                ) {
                    AsyncImage(
                        model = item.album.cover,
                        placeholder = painterResource(id = R.drawable.sudoimage),
                        error = painterResource(id = R.drawable.sudoimage),
                        contentDescription = "Album cover",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 2.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 18.sp
                        )
                        Text(
                            text = item.artist.name,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}





