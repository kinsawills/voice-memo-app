package com.example.voicememoapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.voicememoapp.ui.MemoViewModel

@Composable
fun RecordMemoButton(modifier: Modifier, viewModel: MemoViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val isRecording = uiState.isRecording

    Column(modifier.fillMaxWidth()) {
        FloatingActionButton(
            onClick = {
                if (isRecording) viewModel.stopRecording() else viewModel.startRecording()
            },
            containerColor = Color.Red,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Mic else Icons.Default.PlayArrow,
                contentDescription = if (isRecording) "Stop recording" else "Start recording",
                tint = Color.White
            )
        }
    }
}
