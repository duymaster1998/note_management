package com.example.note_management.models

import java.io.Serializable

data class NoteModel(
    var id: Long? = null,
    var note: String? = null,
) : Serializable
