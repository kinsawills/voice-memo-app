package com.example.voicememoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voicememoapp.data.Folder
import com.example.voicememoapp.data.FolderDao
import com.example.voicememoapp.data.Memo
import com.example.voicememoapp.data.MemoDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val folderDao: FolderDao,
    private val memoDao: MemoDao
) : ViewModel() {
    private val _folders = MutableStateFlow<List<Folder>>(arrayListOf())
    val folders get() = _folders.asStateFlow().value

    private val _memos = MutableStateFlow<List<Memo>>(arrayListOf())
    val memos get() = _memos.asStateFlow().value

    private val _uiState = MutableStateFlow(MemoUiState(folders, memos))
    val uiState: StateFlow<MemoUiState> = _uiState


    init {
        initializeUIState()
    }

    private fun initializeUIState() {
        _uiState.value =
            MemoUiState(
                folders = folders,
                memos = memos,
                currentSelectedFolder = null
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

    fun updateFolderByName(folderName: String, folderNames: List<String?>) {
        val folders: MutableList<Folder> = mutableListOf<Folder>()

        val folderNameToFolderMap: MutableMap<String, Folder> = mutableMapOf()
        folders.mapIndexed { index, folder ->
            val indexFolder = folderNames[index]
            if (indexFolder != null) {
                folderNameToFolderMap[indexFolder] = folder
            }
        }

        val searchFolder = folderNames.find{folder -> folder == folderName}

        if (searchFolder != null) {
            val updateFolder = folderNameToFolderMap[searchFolder]
            _uiState.update {
                it.copy(
                    currentSelectedFolder = updateFolder,
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
        }
    }

    fun addFolderToDB(name: String) {
        viewModelScope.launch {
            folderDao.insert(Folder(name=name))
        }
    }
}