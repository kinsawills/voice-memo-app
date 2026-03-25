package com.example.voicememoapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** The button for recording memos **/
@Composable
fun RecordMemoButton(modifier: Modifier) {
    Column(modifier.fillMaxWidth()) {
    FloatingActionButton(
        onClick = { /* action */ },
        containerColor = Color.Red,
        modifier = Modifier.align(Alignment.CenterHorizontally)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = Color.White
        )
    }
        }
}