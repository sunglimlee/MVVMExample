package com.example.mvvmexample

import androidx.lifecycle.LiveData
import androidx.room.*

//Dao는 인터페이스, Dao가 모든걸 만들어준다. 우리는 어떤 작업을 할건지만 정하면 된다.
//@Insert 로 이 insert 함수를 가지고 Dao를 연결하겠다는거네.. 나는 insert 함수만 쓰면 되는거고.. Room이 들어오는 noteEntity를 통해서
//모든 SQL 작업을 해준다는거네..
@Dao
interface NoteDao {
    @Insert
    fun insert(noteEntity : NoteEntity)
    @Update
    fun update(noteEntity : NoteEntity)
    @Delete
    fun delete(noteEntity : NoteEntity)
    @Query("DELETE FROM note_table")
    fun deleteAllNote()
    //그러면 LiveData를 해주기만 함으로써 note_table의 값이 바뀌면 바로 반영해주는건가? 전체 데이터를 보여주는걸 다시 Refresh해서 보여준다는 거지?
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    fun getAllNotes() : LiveData<List<NoteEntity>>
}