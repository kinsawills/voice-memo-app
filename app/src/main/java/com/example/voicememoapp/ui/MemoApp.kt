package com.example.voicememoapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.voicememoapp.ui.navigation.AppNavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MemoApp(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    val context = LocalContext.current
    val microphonePermissionsState = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    )

    if(microphonePermissionsState.status.isGranted) {
        AppNavHost(navController = navController)
    }
    else {
        LaunchedEffect(Unit) {
            microphonePermissionsState.launchPermissionRequest()
        }
    }

}