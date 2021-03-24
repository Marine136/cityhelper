package ipvc.estg.cityhelper

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.cityhelper.entities.Note
import ipvc.estg.cityhelper.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_add_note.*

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var editNoteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        editNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        editNoteTitle.setText(intent.getStringExtra(EXTRA_TITLE))
        editNoteDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))

        val button = findViewById<Button>(R.id.buttonUpdateNote)
        button.setOnClickListener {
            updateNote()
        }

    }

    private fun updateNote() {
        val titulo = editNoteTitle.text.toString()
        val descricao = editNoteDescription.text.toString()

        if (inputCheck(titulo, descricao)) {
            // Create Note Object
            val updatedNote = Note(-1, titulo, descricao)
            // Add Data to Database
            editNoteViewModel.updateNote(updatedNote)
            Toast.makeText(this, R.string.successUpdate, Toast.LENGTH_LONG).show()
            finish()

        } else {
            Toast.makeText(this, R.string.insuccess, Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(titulo: String, descricao: String): Boolean {
        return !(TextUtils.isEmpty(titulo) || TextUtils.isEmpty(descricao))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_back_button, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.back_button -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_ID = "ipvc.estg.noteapp.ID"
        const val EXTRA_TITLE = "ipvc.estg.noteapp.TITLE"
        const val EXTRA_DESCRIPTION = "ipvc.estg.noteapp.DESCRIPTION"
    }

}