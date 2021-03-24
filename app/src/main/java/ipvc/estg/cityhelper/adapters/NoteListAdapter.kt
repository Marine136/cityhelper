package ipvc.estg.cityhelper.adapters

import android.app.PendingIntent.getActivity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Update
import ipvc.estg.cityhelper.NotesActivity
import ipvc.estg.cityhelper.R
import ipvc.estg.cityhelper.UpdateNoteActivity
import ipvc.estg.cityhelper.entities.Note
import kotlinx.android.synthetic.main.recyclerview_item.view.*


class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.MyViewHolder>() {

    private var noteList = emptyList<Note>()
    private lateinit var listener:OnItemClickListener


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = noteList[position]
        holder.itemView.id_noteTitle.text = currentItem.titulo
        holder.itemView.id_noteDescription.text = currentItem.descricao

        holder.itemView.itemLayout.setOnClickListener{
            fun onClick() {
                if (listener != null && position != RecyclerView.NO_POSITION)
                listener.onItemClick(currentItem)
            }
        }
    }

    internal fun setData(note: List<Note>){
        this.noteList = note
        notifyDataSetChanged()
    }
    internal fun getNoteAt(position: Int): Note {
        return noteList[position]
    }

    interface OnItemClickListener {
        fun onItemClick(note:Note)
    }

    fun setOnItemClickListener(listener:OnItemClickListener) {
        this.listener = listener
    }
}