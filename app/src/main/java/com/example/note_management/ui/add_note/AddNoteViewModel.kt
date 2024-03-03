package com.example.note_management.ui.add_note

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.note_management.helper.NoteDBHelper
import com.example.note_management.models.NoteModel

class AddNoteViewModel : ViewModel() {
    private lateinit var db: NoteDBHelper

    fun setUp(context: Context) {
        db = NoteDBHelper(context)
    }

    fun addNote(note: NoteModel) =
        db.insertNote(note)
}