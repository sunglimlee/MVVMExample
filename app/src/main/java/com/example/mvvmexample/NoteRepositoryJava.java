package com.example.mvvmexample;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

class NoteRepositoryJava {
    private NoteDao  noteDao;
    private LiveData<List<NoteEntity>> allNotes;

    public NoteRepositoryJava(Application application) {
        NoteDatabaseJava database = NoteDatabaseJava.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }
    public void insert(NoteEntity note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(NoteEntity note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    public void update(NoteEntity note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }
    public void deleteAllNotes() {
        new DeleteAllNoteAsyncTask(noteDao).execute();
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.update(noteEntities[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.delete(noteEntities[0]);
            return null;
        }
    }
    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private DeleteAllNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Void...voids) {
            noteDao.deleteAllNote();
            return null;
        }
    }
    private static class InsertNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.insert(noteEntities[0]);
            return null;
        }
    }
}
