package ipvc.estg.cityhelper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import ipvc.estg.cityhelper.adapters.NoteListAdapter
import ipvc.estg.cityhelper.entities.Note
import ipvc.estg.cityhelper.viewmodel.NoteViewModel


class NotesActivity : AppCompatActivity() {

    private lateinit var editNoteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        //recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //view model
        editNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        editNoteViewModel.allNotes.observe(this, Observer { note ->
            adapter.setData(note)
        })

        adapter.setOnItemClickListener(object:NoteListAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                Intent(this@NotesActivity, UpdateNoteActivity::class.java)
                intent.putExtra(UpdateNoteActivity.EXTRA_TITLE, note.titulo)
                intent.putExtra(UpdateNoteActivity.EXTRA_DESCRIPTION, note.titulo)
                intent.putExtra(UpdateNoteActivity.EXTRA_ID, note.id)
                startActivity(intent)
            }
        })

        /*    DELETE ON SWIPE
        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder, target: ViewHolder
                ): Boolean {
                    val fromPos = viewHolder.adapterPosition
                    val toPos = target.adapterPosition
                    // move item in `fromPos` to `toPos` in adapter.
                    return true // true if moved, false otherwise
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    editNoteViewModel.deleteNote(adapter.getNoteAt(viewHolder.adapterPosition))
                    Toast.makeText(this@NotesActivity, "Nota apagada", Toast.LENGTH_SHORT).show()
                }
            })
*/


        //bottom nav bar
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.notes

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.map -> {
                    finish()
                    startActivity(Intent(applicationContext, MapActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.notes -> {
                    Toast.makeText(this, R.string.notes, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.settings -> {
                    finish()
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }

     fun onItemClick(position: Int) {
        Toast.makeText(this@NotesActivity, "TEST: " + position, Toast.LENGTH_SHORT).show()
    }

    //Botão de adicionar nota
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_add_note, menu)
        return true
    }
    //Botão de adicionar nota
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_note -> {
                startActivity(Intent(this, AddNoteActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}