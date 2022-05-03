@file:Suppress("DEPRECATION")

package com.example.mvvmexample

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData

@Suppress("unused")
class NoteRepositoryKt(application : Application) {
    //여기 보다시피 noteDao를 사용하는게 제일 중요하다. 이 객체를
    private var noteDao : NoteDao
    private var allNotes : LiveData<List<NoteEntity>>

    init { //여기가 noteDao가 장착되는부분
        //************ 이부분이 모든걸 설명해주네.. ***************** 데이터베이스 만들고, 그데이터베이스에서 noteDao 얻고 그 noteDao로 데이터를 받는거네..
        val database : NoteDatabaseKt = NoteDatabaseKt.getInstance(application)
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }
    //여기서 다시 AsyncTask를 사용하는 이유는 이러한 작업을 백그라운드에서 해주기 위해서이다.
    fun insert(note : NoteEntity) {InsertNoteAsyncTask(noteDao).execute(note)}
    fun update(note : NoteEntity) {UpdateNoteAsyncTask(noteDao).execute(note)}
    fun delete(note : NoteEntity) {DeleteNoteAsyncTask(noteDao).execute(note)}
    fun deleteAllNotes() {DeleteAllNoteAsyncTask(noteDao).execute()}
    fun getAllNotes() : LiveData<List<NoteEntity>> {
        return allNotes
    }
    // 나중에 이부분을 상속으로 해결해봐라..
    private class InsertNoteAsyncTask(private var noteDao : NoteDao) : AsyncTask<NoteEntity, Unit, Unit>() {
        override fun doInBackground(vararg p0: NoteEntity?) {
            noteDao.insert(p0[0]!!)
        }
    }
    private class UpdateNoteAsyncTask(private var noteDao : NoteDao) : AsyncTask<NoteEntity, Unit, Unit>() {
        override fun doInBackground(vararg p0: NoteEntity?) {
            noteDao.update(p0[0]!!)
            Log.e("NoteRepositoryUpdate", "실행되었슴")
        }
    }
    private class DeleteNoteAsyncTask(private var noteDao : NoteDao) : AsyncTask<NoteEntity, Unit, Unit>() {
        override fun doInBackground(vararg p0: NoteEntity?) {
            noteDao.delete(p0[0]!!)
        }
    }
    private class DeleteAllNoteAsyncTask(private var noteDao : NoteDao) : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg p0: Unit?) {
            noteDao.deleteAllNote()
        }
    }
}