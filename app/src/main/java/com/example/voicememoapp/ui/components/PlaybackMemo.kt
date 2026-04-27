package com.example.voicememoapp.ui.components

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.voicememoapp.data.Memo


@Composable
fun PlaybackMemo(memo: Memo, modifier: Modifier = Modifier) {
    var isPlaying by remember { mutableStateOf(false) }
    var showTranscription by rememberSaveable { mutableStateOf(false) }
    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    if (showTranscription) {
        AlertDialog(
            onDismissRequest = { showTranscription = false },
            title = { Text(memo.name) },
            text = { Text(memo.transcription.ifBlank { "No transcription available." }) },
            confirmButton = {
                TextButton(onClick = { showTranscription = false }) { Text("Close") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        Card(modifier = Modifier.fillMaxWidth().clickable { showTranscription = true }) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    isPlaying = !isPlaying
                    if (isPlaying) {
                        try {
                            val file = java.io.File(memo.filePath)
                            Log.d("PlaybackMemo", "path=${memo.filePath} exists=${file.exists()} size=${file.length()}")
                            mediaPlayer.reset()
                            mediaPlayer.setAudioAttributes(
                                android.media.AudioAttributes.Builder()
                                    .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SPEECH)
                                    .build()
                            )
                            mediaPlayer.setDataSource(memo.filePath)
                            mediaPlayer.prepare()
                            mediaPlayer.start()
                            mediaPlayer.setOnCompletionListener { isPlaying = false }
                        } catch (e: Exception) {
                            Log.e("PlaybackMemo", "Playback failed for ${memo.filePath}", e)
                            isPlaying = false
                        }
                    } else {
                        if (mediaPlayer.isPlaying) mediaPlayer.pause()
                    }
                }) {
                    AnimatedContent(targetState = isPlaying) { playing ->
                        if (playing) {
                            Icon(Icons.Filled.Pause, contentDescription = "Pause")
                        } else {
                            Icon(Icons.Filled.PlayArrow, contentDescription = "Play")
                        }
                    }
                }
                Text(memo.name)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
