package ipvc.estg.cityhelper.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cityhelper.adapters.NoteAdapter
import ipvc.estg.cityhelper.viewmodel.NoteViewModel

class NoteItemTouchHandler(val noteViewModel: NoteViewModel, val adapter: NoteAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        noteViewModel.delete(adapter.getItemAt(viewHolder.adapterPosition))
    }
}