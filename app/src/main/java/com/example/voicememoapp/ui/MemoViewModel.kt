package com.example.voicememoapp.ui

import androidx.lifecycle.ViewModel
import com.example.voicememoapp.data.Folder
import com.example.voicememoapp.data.Memo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MemoViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MemoUiState())
    val uiState: StateFlow<MemoUiState> = _uiState

    init {
        initializeUIState()
    }

    private fun initializeUIState() {
        val folders: MutableList<Folder> = mutableListOf <Folder>() // finish this when we can store memos
        val memos: MutableMap<Long, MutableList<Memo>> = mutableMapOf() // finish this when we can store memos
        _uiState.value =
            MemoUiState(
                folders = folders,
                memos = memos,
                currentSelectedFolder = folders[0]
            )
    }

    fun updateCurrentFolderState(folder: Folder) {
        _uiState.update {
            it.copy(
                currentSelectedFolder = folder,
                isShowingHomepage = false
            )
        }
    }

    fun resetHomeScreenStates() {
        _uiState.update {
            it.copy(
                currentSelectedFolder = it.folders[0],
                isShowingHomepage = true
            )
        }
    }
}