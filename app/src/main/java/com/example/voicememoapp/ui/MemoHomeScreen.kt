package com.example.voicememoapp.ui

import SimpleSearchBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.voicememoapp.data.Folder
import com.example.voicememoapp.ui.components.RecordMemoButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.map

/** The main page that has the list of folders **/

@Composable
fun MemoHomeScreen(
    uiState: MemoUiState,
    viewModel: MemoViewModel,
    modifier: Modifier = Modifier
) {
    val folders = uiState.folders
    val folderNames = folders.map{folder -> folder?.name}
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ){
        SimpleSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = {viewModel.updateFolderByName(folderName = searchQuery, folderNames = folderNames)},
            onResultClick = { searchQuery = it },
            searchResults = folderNames,
            placeholder = { Text("Search Folders") }
        )
        Button(
            onClick = { viewModel.addFolderToDB("testFolder") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add Folder")
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f)
        ) {
            items(folders) { folder ->
                FolderCard(folder)
            }
        }
            RecordMemoButton(modifier)


    }
}

@Composable
fun FolderCard(folder: Folder?, modifier: Modifier = Modifier) {
    if (folder != null) {
        Card(modifier = Modifier.padding(2.dp)) {
            Text("$folder.name")
        }
    }
}