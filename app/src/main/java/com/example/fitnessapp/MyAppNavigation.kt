package com.example.fitnessapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnessapp.pages.HomePage
import com.example.fitnessapp.pages.LoginPage
import com.example.fitnessapp.pages.MusicPage
import com.example.fitnessapp.pages.PlayerPage
import com.example.fitnessapp.pages.ProfilePage
import com.example.fitnessapp.pages.ProgressPage
import com.example.fitnessapp.pages.SignupPage
@Composable
fun MyAppNavigation(modifier:Modifier=Modifier,viewModel:MusicViewModel){
    val navController=rememberNavController()


    val isLoggedIn = viewModel.isUserLoggedIn()


    val startDestination = if (isLoggedIn) "MainPage" else "LoginPage"


    NavHost(navController = navController, startDestination = startDestination)
    {
        composable("MainPage")
        {
            MainPage(modifier,viewModel,navController)
        }
        composable("LoginPage")
        {
            LoginPage(modifier,navController,viewModel)
        }
        composable("SignupPage")
        {
            SignupPage(modifier,navController,viewModel)
        }
        composable("HomePage"){
            HomePage(modifier,navController,viewModel)
        }
        composable("ProfilePage") {
            ProfilePage(modifier,viewModel,navController)
        }

        composable("MusicPage"){
            MusicPage(modifier,navController,viewModel)
        }
        composable("ProgressPage")
        {
            ProgressPage(navController, viewModel)
        }

        composable("PlayerPage/{cover}/{name}/{link}") { backStackEntry ->
            val cover = backStackEntry.arguments?.getString("cover") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val link = backStackEntry.arguments?.getString("link") ?: ""
            PlayerPage(modifier,navController,viewModel,name = name, cover = cover, link = link)
        }


    }
}

