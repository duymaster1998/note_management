package com.example.note_management.ui.add_note

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.note_management.databinding.ActivityAddNoteBinding
import com.example.note_management.models.NoteModel


class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private val viewModel: AddNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUp(this)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            btnAdd.setOnClickListener {
                val noteModel = NoteModel(note = edtNote.text.toString().trim())
                val id = viewModel.addNote(noteModel)
                val intent = Intent("ACTION_UPDATE_NOTES")
                intent.putExtra("note", noteModel.copy(id = id))
                sendBroadcast(intent)
                finish()
            }
            btnAdd.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    btnAdd.isEnabled = !p0.isNullOrEmpty()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }

}