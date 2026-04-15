package com.example.voicememoapp.ui

import SimpleSearchBar
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.voicememoapp.ui.components.PlaybackMemo
import com.example.voicememoapp.ui.components.RecordMemoButton

/** The screen that displays the list of memos inside of a selected folder **/
@Composable
fun MemoFolderContentScreen(
    uiState : MemoUiState,
    viewModel : MemoViewModel,
    onBackPressed: () -> Unit,
    modifier : Modifier = Modifier
) {
    val folders = uiState.folders
    val memos = uiState.currentFolderMemos
    val folderNames = folders.map{folder -> folder.name}
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val filteredFolderNames = if (searchQuery.isBlank()) emptyList() else folderNames.filter { it.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackHandler {
            onBackPressed()
        }
        SimpleSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = {
                viewModel.updateFolderByName(
                    folderName = searchQuery,
                    folderNames = folderNames
                )
            },
            onResultClick = { searchQuery = it },
            searchResults = filteredFolderNames,
            placeholder = { Text("Search Folders") }
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .weight(1f)
        ) {
            items (memos) { memo ->
                PlaybackMemo(memo = memo)
            }
        }
        RecordMemoButton(modifier, viewModel)
    }
}