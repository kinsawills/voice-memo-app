package com.example.voicememoapp.ui

import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicememoapp.data.Folder
import com.example.voicememoapp.data.FolderDao
import com.example.voicememoapp.data.Memo
import com.example.voicememoapp.data.MemoDao
import com.example.voicememoapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val folderDao: FolderDao,
    private val memoDao: MemoDao
) : ViewModel() {
    val apiKey = BuildConfig.MY_API_KEY
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: String = ""
    private val _folders = MutableStateFlow<List<Folder>>(arrayListOf())
    val folders get() = _folders.asStateFlow().value

    private val _memos = MutableStateFlow<List<Memo>>(arrayListOf())
    val memos get() = _memos.asStateFlow().value

    private val _uiState = MutableStateFlow(MemoUiState(folders, memos))
    val uiState: StateFlow<MemoUiState> = _uiState


    init {
        allFoldersFromDB()
        allMemosFromDB()
    }

    fun updateCurrentFolderState(folder: Folder) {
        _uiState.update {
            it.copy(
                currentSelectedFolder = folder,
                isShowingHomepage = false
            )
        }
    }

    fun updateFolderByName(folderName: String, folderNames: List<String?>) {
        val matchedFolder = _folders.value.find { it.name == folderName }
        if (matchedFolder != null) {
            _uiState.update {
                it.copy(
                    currentSelectedFolder = matchedFolder,
                    isShowingHomepage = false
                )
            }
        }
    }

    fun resetHomeScreenStates() {
        _uiState.update {
            it.copy(
                currentSelectedFolder = null,
                isShowingHomepage = true
            )
        }
    }

    fun allFoldersFromDB() {
        viewModelScope.launch {
            _folders.value = folderDao.getAllFolders()
            _uiState.update { it.copy(folders = _folders.value) }
        }
    }

    fun addFolderToDB(name: String) {
        viewModelScope.launch {
            folderDao.insert(Folder(name=name))
            _folders.value = folderDao.getAllFolders()
            _uiState.update { it.copy(folders = _folders.value) }
        }
    }

    fun allMemosFromDB() {
        viewModelScope.launch {
            _memos.value = memoDao.getAllMemos()
            _uiState.update { it.copy(memos = _memos.value) }
        }
    }

    suspend fun transcribeAudio(file: File, apiKey: String): String {
        val model = GenerativeModel(
            modelName = "gemini-3.1-flash-lite-preview",
            apiKey = apiKey
        )

        val audioBytes = file.readBytes()

        return try {
            val response = model.generateContent(
                content {
                    blob("audio/mp4", audioBytes)
                    text("transcribe this audio recording. Return only the transcription text.")
                }
            )
            response.text ?: "Transcription failed"
        } catch (e: Exception) {
            Log.e("Transcription", "Failed to transcribe audio", e)
            "Transcription unavailable"
        }
    }

    suspend fun addMemoToDB(name: String, folderId: Int, filePath: String, transcript: String = "") {
        memoDao.insert(Memo(
            name = name,
            folderId = folderId,
            filePath = filePath,
            transcription = transcript
        ))
        _memos.value = memoDao.getMemosByFolderId(folderId)
        _uiState.update { it.copy(memos = _memos.value) }
    }

    fun startRecording() {
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
        _uiState.update { it.copy(isRecording = true) }
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        val file = File(outputFile)
        val folderId = uiState.value.currentSelectedFolder?.id ?: -1
        val path = outputFile

        _uiState.update { it.copy(isRecording = false) }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isTranscribing = true) }
            val transcription = transcribeAudio(file, apiKey)
            Log.d("Transcription", transcription)
            // Instead of saving immediately, store as pending and show dialog
            _uiState.update {
                it.copy(
                    isTranscribing = false,
                    pendingMemo = PendingMemo(
                        filePath = path,
                        folderId = folderId,
                        transcription = transcription
                    )
                )
            }
        }
    }

    fun saveMemoWithName(name: String) {
        val pending = uiState.value.pendingMemo ?: return
        val finalName = name.ifBlank { UUID.randomUUID().toString().take(6) }
        viewModelScope.launch {
            addMemoToDB(
                name = finalName,
                folderId = pending.folderId,
                filePath = pending.filePath,
                transcript = pending.transcription
            )
            _uiState.update { it.copy(pendingMemo = null) }
        }
    }

    fun dismissNamingDialog() {
        // Discard the pending memo entirely, or you could still save it with a generated name
        _uiState.update { it.copy(pendingMemo = null) }
    }

    override fun onCleared() {
        super.onCleared()
        mediaRecorder?.release()
        mediaRecorder = null
    }
}