package com.example.mvvmexample

import androidx.room.Entity
import androidx.room.PrimaryKey

// Room annotation 을 많이 사용한다.
@Entity(tableName = "note_table")
data class NoteEntity(private val title : String, private val description: String, private val priority : Int) {

    @PrimaryKey(autoGenerate = true)
    private var id : Int = 0

    fun setId(id : Int) {
        this.id = id
    }
    fun getId() : Int {
        return id
    }
    fun getTitle() : String {
        return title
    }
    fun getDescription() : String {
        return description
    }
    fun getPriority() : Int {
        return priority
    }

}