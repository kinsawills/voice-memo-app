package com.example.voicememoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.voicememoapp.ui.MemoHomeScreen
import com.example.voicememoapp.ui.MemoViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: MemoViewModel,
    startDestination: String = "home"
){
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){

        composable("home") {
            val uiState by viewModel.uiState.collectAsState()
            MemoHomeScreen(
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }
}