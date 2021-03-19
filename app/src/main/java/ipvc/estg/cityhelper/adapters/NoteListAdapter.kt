package ipvc.estg.cityhelper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cityhelper.R
import ipvc.estg.cityhelper.entities.Note
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.MyViewHolder>() {

    private var noteList = emptyList<Note>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false))
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = noteList[position]
        holder.itemView.id_noteTitle.text = currentItem.titulo
        holder.itemView.id_noteDescription.text = currentItem.descricao
    }


    internal fun setData(note: List<Note>){
        this.noteList = note
        notifyDataSetChanged()
    }
}