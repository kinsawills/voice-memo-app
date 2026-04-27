package com.example.voicememoapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.voicememoapp.ui.MemoViewModel

@Composable
fun RecordMemoButton(modifier: Modifier, viewModel: MemoViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val isRecording = uiState.isRecording
    val isTranscribing = uiState.isTranscribing
    val pendingMemo = uiState.pendingMemo

    // Show naming dialog when a memo is ready to be named
    if (pendingMemo != null) {
        MemoNamingDialog(
            onConfirm = { name -> viewModel.saveMemoWithName(name) },
            onDismiss = { viewModel.dismissNamingDialog() }
        )
    }

    Column(modifier.fillMaxWidth()) {
        FloatingActionButton(
            onClick = {
                if (!isTranscribing) {
                    if (isRecording) viewModel.stopRecording() else viewModel.startRecording()
                }
            },
            containerColor = Color.Red,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            if (isTranscribing) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Mic else Icons.Default.PlayArrow,
                    contentDescription = if (isRecording) "Stop recording" else "Start recording",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun MemoNamingDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var memoName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Name your memo") },
        text = {
            OutlinedTextField(
                value = memoName,
                onValueChange = { memoName = it },
                label = { Text("Memo name") },
                placeholder = { Text("e.g. Meeting notes") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(memoName) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Discard")
            }
        }
    )
}
