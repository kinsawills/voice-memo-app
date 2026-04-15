package com.example.voicememoapp.ui

import com.example.voicememoapp.data.Folder
import com.example.voicememoapp.data.Memo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MemoUiState(
    val folders: List<Folder>,
    val memos: List<Memo>,
    val currentSelectedFolder: Folder? = null,
    val isShowingHomepage: Boolean = true
    ) {
    val currentFolderMemos: List<Memo> by lazy { memos.filter { memo -> memo.folderId == (currentSelectedFolder?.id ?: -1)   } }
}