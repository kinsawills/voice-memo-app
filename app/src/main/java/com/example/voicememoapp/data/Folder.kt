package com.example.voicememoapp.data

import androidx.annotation.StringRes

data class Folder(
    val id: Long,
    @StringRes val name: Int = -1
)
