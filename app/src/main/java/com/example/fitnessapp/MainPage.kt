package com.example.fitnessapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.fitnessapp.pages.HomePage
import com.example.fitnessapp.pages.MusicPage
import com.example.fitnessapp.pages.NotificationPage
import com.example.fitnessapp.pages.ProfilePage

@Composable
fun MainPage(modifier:Modifier=Modifier,viewModel:MusicViewModel,navController: NavController){

    val  navItemList= listOf(
        NavItem("Home",Icons.Default.Home),
        NavItem("Song",Icons.Default.PlayArrow),
        NavItem("Profile",Icons.Default.AccountCircle)
    )
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
        , bottomBar = {
            NavigationBar {

                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(selected = selectedIndex==index
                        , onClick = {
                            selectedIndex=index
                        }
                        , icon = {
                            Icon(imageVector = navItem.icon, contentDescription ="icon" )
                        }
                        , label = {
                            Text(text = navItem.lable)
                        })
                }

            }
        }
    ) { innerPadding ->
        ContentScreen(modifier=Modifier.padding(innerPadding),selectedIndex,viewModel,navController)
    }
}
@Composable
fun ContentScreen(modifier: Modifier,selectedIndex:Int,viewModel:MusicViewModel,navController: NavController){
    when(selectedIndex){
        0-> HomePage(modifier,navController,viewModel)
        1-> MusicPage(modifier,navController,viewModel)
        2-> ProfilePage(modifier,viewModel,navController)
    }
}
