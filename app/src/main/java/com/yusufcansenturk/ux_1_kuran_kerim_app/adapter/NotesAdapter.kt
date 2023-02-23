package com.yusufcansenturk.ux_1_kuran_kerim_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.ItemNoteRowBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.Notes.Notes
import kotlinx.android.synthetic.main.item_note_row.view.*

class NotesAdapter(val notesLists :List<Notes>) : RecyclerView.Adapter<NotesAdapter.NotesHolder>() {

    private val colors: Array<String> = arrayOf("#f94144","#f3722c","#f8961e","#f9844a","#f9c74f","#90be6d","#43aa8b","#4d908e","#577590", "#277da1")

    class NotesHolder(private val binding: ItemNoteRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notes: Notes) {
            binding.apply {
                titleText.text = notes.title
                noteText.text = notes.note
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        val binding = ItemNoteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesHolder(binding)
    }

    override fun getItemCount(): Int {
        return notesLists.size
    }

    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        holder.bind(notesLists[position])
        holder.itemView.change_background.setBackgroundColor(android.graphics.Color.parseColor(colors[position % 8]))
    }


}