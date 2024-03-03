package com.example.note_management.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.note_management.helper.NoteDBHelper
import com.example.note_management.models.NoteModel

class MainViewModel : ViewModel() {
    private lateinit var db: NoteDBHelper

    fun setUp(context: Context) {
        db = NoteDBHelper(context)
    }

    fun getAll(): List<NoteModel> {
        return db.allNotes
    }

    fun getNoteByField(input: String): List<NoteModel> {
        return db.getNoteByField(input)
    }
}