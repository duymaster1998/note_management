package com.example.note_management.ui.edit_note

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.note_management.helper.NoteDBHelper
import com.example.note_management.models.NoteModel

class EditNoteViewModel : ViewModel() {
    private lateinit var db: NoteDBHelper

    fun setUp(context: Context) {
        db = NoteDBHelper(context)
    }

    fun updateNote(note: NoteModel) {
        db.updateNote(note)
    }

    fun getNote(id: Long): NoteModel {
        return db.getNoteById(id)
    }
}