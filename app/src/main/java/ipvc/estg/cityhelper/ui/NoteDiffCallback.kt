package ipvc.estg.cityhelper.ui

import androidx.recyclerview.widget.DiffUtil
import ipvc.estg.cityhelper.entities.Note


class NoteDiffCallback: DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.description == newItem.description
    }
}