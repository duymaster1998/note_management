package com.example.note_management.ui.edit_note

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.note_management.databinding.ActivityEditNoteBinding
import com.example.note_management.models.NoteModel

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private val viewModel: EditNoteViewModel by viewModels()
    private var noteId : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUp(this)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteId = intent.getLongExtra("note_id", -1)

        if (noteId == (-1).toLong()) {
            finish()
            return
        }
        val note = viewModel.getNote(noteId)
        binding.apply {
            note.note?.let { edtNote.setText(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            btnEdit.setOnClickListener {
                val noteModel = NoteModel(id = noteId, note = edtNote.text.toString())
                viewModel.updateNote(noteModel)
                val intent = Intent("ACTION_UPDATE_NOTE")
                intent.putExtra("note", noteModel)
                sendBroadcast(intent)
                finish()
            }
            edtNote.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    btnEdit.isEnabled = !p0.isNullOrEmpty()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
        }
    }

}