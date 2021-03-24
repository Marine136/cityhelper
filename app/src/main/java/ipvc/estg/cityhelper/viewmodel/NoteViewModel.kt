package ipvc.estg.cityhelper.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ipvc.estg.cityhelper.db.NoteRepository
import ipvc.estg.cityhelper.entities.Note


//DO NOT HOLD references to views or contexts! ViewModels are retained throughout lifecycle configuration changes
class NoteViewModel(application: Application): AndroidViewModel(application) {

    private val repository: NoteRepository = NoteRepository(application, viewModelScope)

    fun getAllNotes() = repository.getAllNotes()
    fun insert(note: Note) = repository.insert(note)
    fun update(note: Note) = repository.update(note)
    fun delete(note: Note) = repository.delete(note)
    fun deleteAll() = repository.deleteAll()
}