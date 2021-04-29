package ipvc.estg.cityhelper.db


import android.app.Application
import androidx.lifecycle.LiveData
import ipvc.estg.cityhelper.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NoteRepository(application: Application, private val coroutineScope: CoroutineScope) {

    private val noteDao = NotesDatabase.getDatabase(application, coroutineScope).noteDao()

    fun insert(note: Note) {
        coroutineScope.launch(Dispatchers.IO) { noteDao.insert(note) }
    }

    fun update(note: Note) {
        coroutineScope.launch(Dispatchers.IO) { noteDao.update(note) }
    }

    fun delete(note: Note) {
        coroutineScope.launch(Dispatchers.IO) { noteDao.delete(note) }
    }

    fun deleteAll() {
        coroutineScope.launch(Dispatchers.IO) { noteDao.deleteAllNotes() }
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return noteDao.getAllNotes()
    }
}