package com.example.voicememoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.voicememoapp.ui.MemoHomeScreen
import com.example.voicememoapp.ui.MemoUiState
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
            MemoHomeScreen(
                uiState = MemoUiState(viewModel.folders, viewModel.memos),
                viewModel = viewModel
            )
        }
    }
}