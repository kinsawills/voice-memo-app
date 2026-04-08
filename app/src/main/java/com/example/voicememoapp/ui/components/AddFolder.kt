package com.example.voicememoapp.ui.components

import android.R.attr.label
import android.R.attr.onClick
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.voicememoapp.ui.MemoViewModel

@Composable
fun AddFolder(open: Boolean, setOpen: (Boolean) -> Unit, viewModel: MemoViewModel) {
    var folderName by rememberSaveable {mutableStateOf("")}

    fun onConfirmation() {
        viewModel.addFolderToDB(folderName)
        setOpen(false)
        folderName = ""
    }

    if (open) {
        Dialog(onDismissRequest = { setOpen(false) }) {
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Add Folder", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    OutlinedTextField(
                        value = folderName,
                        onValueChange = { folderName = it },
                        label = { Text("Folder Name") }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { onConfirmation() },
                        ) { Text("Confirm") }
                    }
                }
            }
        }
    } else {
        Button(
            onClick = { setOpen(!open) },
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Folder")
            Text("Folder", modifier = Modifier.padding(start = 8.dp))
        }
    }
}