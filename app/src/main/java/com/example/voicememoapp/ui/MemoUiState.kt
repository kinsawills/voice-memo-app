package com.example.voicememoapp.ui

import com.example.voicememoapp.data.Folder
import com.example.voicememoapp.data.Memo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MemoUiState(
    val folders: List<Folder>,
    val memos: List<Memo>,
    val currentSelectedFolder: Folder? = null,
    val isShowingHomepage: Boolean = true,
    val isRecording: Boolean = false,
    val isTranscribing: Boolean = false,
    val pendingMemo: PendingMemo? = null
    ) {
    val currentFolderMemos: List<Memo> by lazy { memos.filter { memo -> memo.folderId == (currentSelectedFolder?.id ?: -1)   } }
}
data class PendingMemo(
    val filePath: String,
    val folderId: Int,
    val transcription: String
)
