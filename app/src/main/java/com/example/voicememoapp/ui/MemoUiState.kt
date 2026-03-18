package com.example.voicememoapp.ui

import com.example.voicememoapp.data.Folder
import com.example.voicememoapp.data.Memo

data class MemoUiState(
    val folders: MutableList<Folder> = mutableListOf<Folder>(),
    val memos: MutableMap<Long, MutableList<Memo>> = mutableMapOf<Long, MutableList<Memo>>(),
    val currentSelectedFolder: Folder? = null,
    val isShowingHomepage: Boolean = true
    ) {
    val currentFolderMemos: List<Memo>? by lazy { memos[currentSelectedFolder?.id] }
}