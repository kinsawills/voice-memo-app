package com.example.voicememoapp.ui.components

import android.R.attr.text
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.util.Log.i
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.voicememoapp.ui.MemoViewModel
import com.example.voicememoapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
//import com.google.ai.client.generativeai.BuildConfig
import java.util.UUID
/** The button for recording memos **/

val apiKey = BuildConfig.MY_API_KEY

suspend fun transcribeAudio(file: File, apiKey: String): String {
    val model = GenerativeModel(
        modelName = "gemini-3.1-flash-lite-preview",
        apiKey = apiKey
    )

    val audioBytes = file.readBytes()

    val response = model.generateContent(
        content {
            blob("audio/mp4", audioBytes)
            text("transcribe this audio recording. Return only the transcription text.")
        }
    )

    return response.text ?: "Transcription failed"
}
@Composable
fun RecordMemoButton(modifier: Modifier, viewModel: MemoViewModel) {
    var isRecording by remember { mutableStateOf(false) }
    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    val context = LocalContext.current
    var outputFile by remember { mutableStateOf("") }
    Column(modifier.fillMaxWidth()) {
    FloatingActionButton(
        onClick = {
            isRecording = !isRecording
            if (isRecording){
                outputFile = "${context.externalCacheDir?.absolutePath}/memo_${System.currentTimeMillis()}.mp3"
                mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    MediaRecorder(context)
                } else {
                    @Suppress("DEPRECATION")
                    MediaRecorder()
                }.apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(outputFile)
                    prepare()
                    start()
                }

            }else{
                mediaRecorder?.apply {
                    stop()
                    val file = java.io.File(outputFile)
                    release()
                }
                mediaRecorder = null

                val file = File(outputFile)

                CoroutineScope(Dispatchers.IO).launch {
                    val transcription = transcribeAudio(file, apiKey)
                    Log.d("Transcription", transcription)
                }
                viewModel.addMemoToDB(
                    name = UUID.randomUUID().toString().take(6),
                    folderId = viewModel.uiState.value.currentSelectedFolder?.id ?: -1,
                    filePath = outputFile

                )

            }
                  },
        containerColor = Color.Red,
        modifier = Modifier.align(Alignment.CenterHorizontally)
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