package com.example.voicememoapp.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.voicememoapp.data.Folder

/** The main page that has the list of folders **/

@Composable
fun MemoHomeScreen(
    uiState: MemoUiState,
    onFolderCardPressed: (Folder) -> Unit,
    modifier: Modifier = Modifier
) {
    val folders = uiState.folders
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        items(folders) { folder ->
            FolderCard(folder)
        }
    }
}

@Composable
fun FolderCard(folder: Folder, modifier: Modifier = Modifier) {
    Card(modifier = Modifier.padding(2.dp)) {
        Text("${folder.name}")
    }
}