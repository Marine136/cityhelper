package ipvc.estg.cityhelper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cityhelper.adapters.NoteAdapter
import ipvc.estg.cityhelper.entities.Note
import ipvc.estg.cityhelper.ui.NoteItemTouchHandler
import ipvc.estg.cityhelper.ui.OnItemClickListener
import ipvc.estg.cityhelper.viewmodel.NoteViewModel


class NotesActivity : AppCompatActivity(), View.OnClickListener, OnItemClickListener {

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

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

        findViewById<FloatingActionButton>(R.id.add_note).setOnClickListener(this)

        val adapter = NoteAdapter()
        adapter.onItemClickListener = this

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        recycler.adapter = adapter

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.getAllNotes().observe(this, Observer { notes ->
            notes?.let { adapter.submitList(notes) }
        })

        ItemTouchHelper(NoteItemTouchHandler(noteViewModel, adapter)).attachToRecyclerView(recycler)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == INTENT_REQUEST_ADD_NOTE && resultCode == RESULT_OK && data != null -> {
                val titleText = data.getStringExtra(EXTRA_TITLE) ?: ""
                val descriptionText = data.getStringExtra(EXTRA_DESCRIPTION) ?: ""

                val note = Note(titleText, descriptionText)
                noteViewModel.insert(note)

                Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show()
            }
            requestCode == INTENT_REQUEST_EDIT_NOTE && resultCode == RESULT_OK && data != null -> {
                val id = data.getIntExtra(EXTRA_ID, -1)
                if (id == -1) {
                    Toast.makeText(this, R.string.insuccess2, Toast.LENGTH_SHORT).show()
                    return
                }

                val title = data.getStringExtra(EXTRA_TITLE) ?: ""
                val description = data.getStringExtra(EXTRA_DESCRIPTION) ?: ""
                noteViewModel.update(Note(title, description, id))

                Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show()
            }
            else -> Toast.makeText(this, R.string.insuccess3, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAll()
                Toast.makeText(this, R.string.delete_all, Toast.LENGTH_SHORT).show()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onClick(view: View) {
        val intent = Intent(this, AddEditNoteActivity::class.java)
        startActivityForResult(intent, INTENT_REQUEST_ADD_NOTE)
    }

    override fun onItemClick(note: Note) {
        val intent = Intent(this, AddEditNoteActivity::class.java)
        intent.putExtra(EXTRA_ID, note.id)
        intent.putExtra(EXTRA_TITLE, note.title)
        intent.putExtra(EXTRA_DESCRIPTION, note.description)
        startActivityForResult(intent, INTENT_REQUEST_EDIT_NOTE)
    }
}