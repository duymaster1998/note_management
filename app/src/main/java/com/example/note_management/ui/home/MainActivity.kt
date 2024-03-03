package com.example.note_management.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.note_management.adapter.NoteAdapter
import com.example.note_management.databinding.ActivityMainBinding
import com.example.note_management.models.NoteModel
import com.example.note_management.ui.add_note.AddNoteActivity
import com.example.note_management.ui.edit_note.EditNoteActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), TextWatcher {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private val viewModel: MainViewModel by viewModels()
    private var job: Job? = null


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("note", NoteModel::class.java)
            } else {
                intent.getSerializableExtra("note") as NoteModel
            }
            if (intent.action != null) {
                when (intent.action) {
                    "ACTION_UPDATE_NOTES" -> {
                        val newList = noteAdapter.currentList + note
                        noteAdapter.submitList(newList.sortedByDescending { it?.id })
                    }

                    "ACTION_UPDATE_NOTE" -> {
                        val newList = noteAdapter.currentList.toMutableList()
                        val currentNote = newList.find {
                            it.id == note?.id
                        }
                        if (currentNote != null) {
                            val index = newList.indexOf(currentNote)
                            newList.removeAt(index)
                            newList.add(index, note)
                        }
                        noteAdapter.submitList(newList.sortedByDescending { it?.id })
                    }
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val filter = IntentFilter()
        filter.addAction("ACTION_UPDATE_NOTES")
        filter.addAction("ACTION_UPDATE_NOTE")
        ContextCompat.registerReceiver(this, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
        viewModel.setUp(this)

        noteAdapter = NoteAdapter {
            val intent = Intent(this, EditNoteActivity::class.java).apply {
                putExtra("note_id", it.id)
            }
            startActivity(intent)
        }


        binding.apply {
            fab.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddNoteActivity::class.java))
            }
            rvNote.adapter = noteAdapter
            edtSearch.addTextChangedListener(this@MainActivity)
        }
        noteAdapter.submitList(viewModel.getAll())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        job?.cancel()
        job = lifecycleScope.launch {
            delay(500)
            val notes = viewModel.getNoteByField(p0.toString().trim())
            noteAdapter.submitList(notes)
        }
    }

    override fun afterTextChanged(p0: Editable?) {

    }
}