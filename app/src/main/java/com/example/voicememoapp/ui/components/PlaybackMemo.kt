package com.example.voicememoapp.ui.components

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.voicememoapp.data.Memo
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp


@Composable
fun PlaybackMemo(memo: Memo, modifier: Modifier = Modifier) {
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    val mediaPlayer = remember { MediaPlayer() }

    Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    isPlaying = !isPlaying
                    if (isPlaying) {
                        val file = java.io.File(memo.filePath)
                        mediaPlayer.reset()
                        mediaPlayer.setAudioAttributes(
                            android.media.AudioAttributes.Builder()
                                .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        mediaPlayer.setDataSource(memo.filePath)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
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
