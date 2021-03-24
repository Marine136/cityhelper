package ipvc.estg.cityhelper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ipvc.estg.cityhelper.R
import ipvc.estg.cityhelper.entities.Note
import ipvc.estg.cityhelper.ui.NoteDiffCallback
import ipvc.estg.cityhelper.ui.NoteViewHolder
import ipvc.estg.cityhelper.ui.OnItemClickListener


class NoteAdapter(): ListAdapter<Note, NoteViewHolder>(NoteDiffCallback()) {

    var onItemClickListener: OnItemClickListener? = null

    fun getItemAt(position: Int): Note {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return NoteViewHolder(this, view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.title.text = note.title
        holder.description.text = note.description
    }
}