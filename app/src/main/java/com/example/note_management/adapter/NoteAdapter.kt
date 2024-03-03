package com.example.note_management.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.note_management.databinding.ItemNoteBinding
import com.example.note_management.models.NoteModel

class NoteAdapter(
    private val listener: (NoteModel) -> Unit
) : ListAdapter<NoteModel, NoteViewHolder>(NoteDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

class NoteViewHolder(
    private val binding: ItemNoteBinding,
    private val listener: (NoteModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NoteModel) {
        binding.apply {
            tvNote.text = item.note
            itemNote.setOnClickListener { listener(item) }
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            listener: (NoteModel) -> Unit
        ): NoteViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: ItemNoteBinding = ItemNoteBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return NoteViewHolder(binding, listener)
        }
    }
}

object NoteDiffCallback : DiffUtil.ItemCallback<NoteModel>() {
    override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
        return oldItem == newItem
    }
}