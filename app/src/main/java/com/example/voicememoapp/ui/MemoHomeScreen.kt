package com.example.voicememoapp.ui

import SimpleSearchBar
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import com.example.voicememoapp.ui.components.AddFolder
import com.example.voicememoapp.ui.components.PlaybackMemo

/** The main page that has the list of folders **/

@Composable
fun MemoHomeScreen(
    uiState: MemoUiState,
    viewModel: MemoViewModel,
    modifier: Modifier = Modifier
) {
    val memos = uiState.currentFolderMemos
    val folders = uiState.folders
    val folderNames = folders.map{folder -> folder.name}
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val filteredFolderNames = if (searchQuery.isBlank()) emptyList() else folderNames.filter { it.contains(searchQuery, ignoreCase = true) }
    var folderDialogOpen by rememberSaveable { mutableStateOf(false)}
    if(uiState.isShowingHomepage) {
      Column(
          modifier = Modifier.fillMaxSize().padding(4.dp),
          horizontalAlignment = Alignment.CenterHorizontally
      ) {
          SimpleSearchBar(
              query = searchQuery,
              onQueryChange = { searchQuery = it },
              onSearch = {
                  viewModel.updateFolderByName(
                      folderName = searchQuery,
                      folderNames = folderNames
                  )
              },
              onResultClick = {
                  searchQuery = it
                  viewModel.updateFolderByName(folderName = it, folderNames = folderNames)
              },
              searchResults = filteredFolderNames,
              placeholder = { Text("Search Folders") }
          )
          AddFolder(open = folderDialogOpen, setOpen = { folderDialogOpen = it }, viewModel)

          LazyVerticalGrid(
              columns = GridCells.Fixed(2),
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp)
                  .weight(1f)
          ) {
              items(folders) { folder ->
                  FolderCard(folder, viewModel = viewModel)
              }
              items(memos) { memo ->
                  PlaybackMemo(memo = memo)
              }
          }
          RecordMemoButton(modifier, viewModel)


      }
  } else {
        MemoFolderContentScreen(
            viewModel = viewModel,
            uiState = uiState,
            onBackPressed = {viewModel.resetHomeScreenStates()}
        )
  }
}

@Composable
fun FolderCard(folder: Folder?, modifier: Modifier = Modifier, viewModel : MemoViewModel) {
    if (folder != null) {
        Card(modifier = Modifier.padding(14.dp), onClick={viewModel.updateCurrentFolderState(folder)}) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Folder,
                    contentDescription = "Folder",
                    tint = Color.Gray
                )
                Text(folder.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}