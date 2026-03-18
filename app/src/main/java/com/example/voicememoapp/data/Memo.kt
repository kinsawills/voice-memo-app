package com.example.voicememoapp.data

import androidx.annotation.StringRes

data class Memo(
    val id: Long,
    val folder: Folder?,
    @StringRes val content: Int = -1,
    @StringRes val transcription: Int = -1,
    @StringRes val createdAt: Int = -1
)
