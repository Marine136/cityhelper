package ipvc.estg.cityhelper.db


import androidx.lifecycle.LiveData
import ipvc.estg.cityhelper.dao.NoteDao
import ipvc.estg.cityhelper.entities.Note

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.readAllData()

    suspend fun addNote(note: Note){
        noteDao.addNote(note)
    }

    suspend fun deleteNote(note: Note){
        noteDao.deleteNote(note)
    }

    suspend fun updateNote(note: Note){
        noteDao.updateNote(note)
    }

}