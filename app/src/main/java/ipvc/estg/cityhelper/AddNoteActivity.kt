package ipvc.estg.cityhelper

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.cityhelper.entities.Note
import ipvc.estg.cityhelper.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_add_note.*
import java.time.LocalDateTime

class AddNoteActivity : AppCompatActivity() {

    private lateinit var editNoteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        editNoteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        val button = findViewById<Button>(R.id.buttonSaveNote)
        button.setOnClickListener{
            insertNoteToDatabase()
        }
    }

    private fun insertNoteToDatabase() {
        val titulo = editNoteTitle.text.toString()
        val descricao = editNoteDescription.text.toString()

        if(inputCheck(titulo, descricao)){
            // Create Note Object
            val note = Note(0, titulo, descricao)
            // Add Data to Database
            editNoteViewModel.addNote(note)
            Toast.makeText(this, R.string.success, Toast.LENGTH_LONG).show()
            finish()

        }else{
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
}