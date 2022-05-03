
package com.example.mvvmexample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {NoteEntity.class}, version = 1)
public abstract class NoteDatabaseJava extends RoomDatabase {
    private static NoteDatabaseJava instance;
    public abstract NoteDao noteDao();

    public static synchronized NoteDatabaseJava getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabaseJava.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }
    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private final NoteDao noteDao;
        private PopulateDbAsyncTask(NoteDatabaseJava db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new NoteEntity("title 1", "Description 1", 1));
            noteDao.insert(new NoteEntity("title 2", "Description 2", 2));
            noteDao.insert(new NoteEntity("title 3", "Description 3", 3));
            return null;
        }
    }
}
