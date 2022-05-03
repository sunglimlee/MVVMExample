package com.example.mvvmexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast

class AddEditNoteActivity : AppCompatActivity() {
    //항상 기억하자.. manifest파일에서 parent activity를 잡아주어야 한다는 걸.. 그럴려고 x 버턴도 만들었잖아.
    //그리고 반드시 MainActivity는 singletop 이 되어야 한다.
    //여기서 보듯이 companion object에 있는 값들도 기본이 public이다.
    companion object {
        const val EXTRA_TITLE : String = "com.example.mvvmexample.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION : String = "com.example.mvvmexample.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY : String = "com.example.mvvmexample.EXTRA_PRIORITY"
        const val EXTRA_ID : String = "com.example.mvvmexample.EXTRA_ID"
    }

    private lateinit var editTextTitle : EditText
    private lateinit var editTextDescription : EditText
    private lateinit var editTextPriority : NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnote)
        editTextDescription = findViewById(R.id.edit__text_description)
        editTextTitle = findViewById(R.id.edit_text_title)
        editTextPriority = findViewById(R.id.number_picker_priority)
        editTextPriority.minValue = 1
        editTextPriority.maxValue = 10

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)

        val intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            editTextPriority.value = intent.getIntExtra(EXTRA_PRIORITY, -1)
        } else {
            title = "Add Note"
        }
    }

    //메뉴 만들어야 한다.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater : MenuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.save_note -> {
            saveNote()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        //알고 있지? 현재의 값들을 전부 저쪽으로 던져줄거다. 근데 지금 Activity로 하니깐 ActivityResult로 넘겨주면 되는거 아냐?
        val title : String = editTextTitle.text.toString()
        val description : String = editTextDescription.text.toString()
        val priority : Int = editTextPriority.value
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a Title and a Description", Toast.LENGTH_SHORT).show()
            return
        }
        val data : Intent = Intent()
        data.putExtra(EXTRA_TITLE, title).putExtra(EXTRA_DESCRIPTION, description).putExtra(
            EXTRA_PRIORITY, priority)
        //Edit Mode를 위해서 ID를 저장하는 부분
        val id : Int = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }
        setResult(RESULT_OK, data)
        finish() //이걸 넣어야지 전의 Acivity의 ActivityForResult가 실행되게 된다.
    }
}



