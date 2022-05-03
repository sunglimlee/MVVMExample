package com.example.mvvmexample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

// ViewModel 이라고 하는데 이건뭔 너무 간단한데??? 점점 간단해 지기는 하네... Entity가 바뀌면 거기만 바꿔주면 되니깐.
// 그러니깐 View와 Model을 연결해주는 통로라는 것 뿐이네.
class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : NoteRepositoryKt = NoteRepositoryKt(application)
    private val allNotes : LiveData<List<NoteEntity>> = repository.getAllNotes()

    fun insert(note : NoteEntity) {
        repository.insert(note)
    }
    fun update(note : NoteEntity) {
        repository.update(note)
    }
    fun delete(note : NoteEntity) {
        repository.delete(note)
    }
    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }
    fun getAllNotes() : LiveData<List<NoteEntity>> {
        return allNotes
    }
}