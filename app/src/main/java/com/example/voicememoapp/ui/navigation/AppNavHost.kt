package com.example.voicememoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.voicememoapp.ui.MemoHomeScreen
import com.example.voicememoapp.ui.MemoUiState

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = "home"
){
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){

        composable("home") {
            MemoHomeScreen(
                uiState = MemoUiState(),
                onFolderCardPressed = {"todo"}
            )
        }
    }
}