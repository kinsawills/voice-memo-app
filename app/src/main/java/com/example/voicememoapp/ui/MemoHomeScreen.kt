package com.example.voicememoapp.ui

import android.R.attr.onClick
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


    Column(
        modifier = Modifier.fillMaxSize()
    ){
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().padding(16.dp).weight(1f)
        ) {
            items(folders) { folder ->
                FolderCard(folder)
            }
        }
        FloatingActionButton(
            onClick = { /* action */ },
            containerColor = Color.Red,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White
            )
        }
//        Button(
//            onClick = {"add implementation for voice recording and photo changing"},
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ){
//            Text("Add Folder")
//        }
    }
}

@Composable
fun FolderCard(folder: Folder, modifier: Modifier = Modifier) {
    Card(modifier = Modifier.padding(2.dp)) {
        Text("${folder.name}")
    }
}