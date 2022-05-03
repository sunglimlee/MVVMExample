@file:Suppress("DEPRECATION")

package com.example.mvvmexample

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

//여기서 Database의 이름이 들어가는 거고 Database 스키마가 들어가는 거네.. DB의 이름이 note_database구나.
@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabaseKt : RoomDatabase() {
    //우리는 NoteDao에 연결하기 위해서 abstract 함수 noteDao()를 만든거다. 이렇게 선언만 해놓은 멤버함수를 만들어놓으면
    //Room이 모든 내용을 채워넣고 우리가 사용할 때 NoteDao객체를 던져준다.
    abstract fun noteDao(): NoteDao

    companion object {
        //single으로 만들려고 정적으로 구현한것임..
        // Singleton prevents multiple instances of database opening at the
        // same time as each RoomDatabase instance is fairly expensive,
        // and you rarely need access to multiple instances within a single process
        @Volatile
        private var instance : NoteDatabaseKt? = null
        //이부분 작업한다고 힘들었지? 이 클래스가 abstract 임으로 Room instance를 바로 만들 수 없다. 그래서 databaseBuilder를 이용하는거다.
        // Gradle 부분에 문제가 있는거 같다.
        // 문제가 생기잖아.. 그러면 대부분 내가 모르는 부분에서 문제가 생긴다.
        // Logcat에서 봤을 때 이부분까지 들어와서 문제가 있다고 나오잖아... 이것처럼.. aused by: java.lang.RuntimeException: cannot find implementation for com.example.mvvmexample.NoteDatabaseKt. NoteDatabaseKt_Impl does not exist

        // *******  해결했다.. **************************************************************
        //    gradle안에 : apply plugin: "kotlin-kapt"
        //    implementation "androidx.room:room-runtime:$room_version"
        //    kapt "androidx.room:room-compiler:$roomVersion"    // ViewModel
        //https://stackoverflow.com/questions/47274677/room-cannot-find-implementation
        // *********************************************************************************
        private const val DB_NAME = "note_database.db"
        @Synchronized fun getInstance(context : Context) : NoteDatabaseKt {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                NoteDatabaseKt::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            }
            return instance as NoteDatabaseKt
        }

        /*
        fun getInstance(context: Context): NoteDatabaseKt
        {
            return instance ?: synchronized(this)
            {
                val dbInstance =  Room.databaseBuilder(context.applicationContext, NoteDatabaseKt::class.java , DB_NAME)
                    .createFromAsset("database/InitialParcelTracker.db")
                    //.addMigrations(MIGRATION) // do migration if you want to get the updated prepopulated database
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
                instance = dbInstance
                dbInstance
            }
        }
*/
        //맨처음 데이터베이스가 만들어지고 난후에 AsyncTask가 실행되어 데이터를 미리 좀 넣기로 하자..
        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance!!).execute()
            }

        }
        class PopulateDbAsyncTask(db: NoteDatabaseKt) : AsyncTask<Void, Void, Void>() {
            private var noteDao : NoteDao = db.noteDao()
            override fun doInBackground(vararg p0: Void?): Void? {
                noteDao.insert(NoteEntity("title 1", "Description 1", 1))
                noteDao.insert(NoteEntity("title 2", "Description 2", 2))
                noteDao.insert(NoteEntity("title 3", "Description 3", 3))
                return null
            }

        }

    }
}