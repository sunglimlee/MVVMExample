package com.example.mvvmexample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    companion object {
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }

    //private val noteViewModel : NoteViewModel by viewModels()
    private lateinit var noteViewModel : NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        //ActivityForResult가 deprecated 되어서 이렇게 작업을 한다.
        val addResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
                val title : String = intent!!.getStringExtra(AddEditNoteActivity.EXTRA_TITLE).toString()
                val description : String = intent!!.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION).toString()
                val priority : Int = intent!!.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)
                val note : NoteEntity = NoteEntity(title,description, priority)
                noteViewModel.insert(note)
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Note not save,", Toast.LENGTH_SHORT).show()
            }
        }
        //Edit mode Launcher
        val editResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
                val id : Int = intent!!.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
                if (id == -1) { //somthin went wrong
                    Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                    return@registerForActivityResult
                }
                val title : String = intent!!.getStringExtra(AddEditNoteActivity.EXTRA_TITLE).toString()
                val description : String = intent!!.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION).toString()
                val priority : Int = intent!!.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)
                val note : NoteEntity = NoteEntity(title,description, priority)
                // update를 하는부분에서 이 라인을 빼면 어디로 업데이트를 할지 모른다.
                note.setId(id)
                noteViewModel.update(note)
                Toast.makeText(this, "Note saved, Edit Mode ID is $id", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Note not save, Edit Mode", Toast.LENGTH_SHORT).show()
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("MVVM Example")
        val buttonAddNote : FloatingActionButton = findViewById(R.id.button_add_note)
        buttonAddNote.setOnClickListener {
            val intent : Intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            addResultLauncher.launch(intent)
        }
        val recyclerView : RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val adapter : NoteAdapter = NoteAdapter()
        recyclerView.adapter = adapter // adapter까지 잡히고나면

        // https://stackoverflow.com/questions/49934658/viewmodel-in-kotlin-unresolved-reference
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        //noteViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(NoteViewModel::class.java)
        //noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        noteViewModel.getAllNotes().observe(this, object : Observer<List<NoteEntity>> {
            // **************** 여기 이벤트가 실시간으로 데이터를 보여주는 결정적인 곳이다. 왜냐하면 observe로 잡아놨으니깐.. ************
            override fun onChanged(t: List<NoteEntity>?) {
                // 항상 null check를 하는걸 습관으로 들이자.. 데이터를 전부 다 지웠을 수 도 있잖아.
                if (t != null) {
                    adapter.submitList(t)
                }
                //Toast.makeText(this@MainActivity, "onChanged", Toast.LENGTH_SHORT).show()
            }
        })
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //진짜 아무것도 아닌데 adapter의 notes에서 position을 받아서 note를 리턴해주는구나.
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Note Deleted", Toast.LENGTH_SHORT).show()
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        //Edit Mode를 Listener로 실행하는 부분
        adapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(position : Int) {
                //AddEditNoteActivity를 오픈한다.
                val noteEntity : NoteEntity = adapter.getNoteAt(position)
                val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, noteEntity.getTitle())
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, noteEntity.getDescription())
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, noteEntity.getPriority())
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, noteEntity.getId())
                Log.e("MainActivityBeforeLunch", noteEntity.getTitle())
                Toast.makeText(this@MainActivity, "Edit Mode" + " Position is ${noteEntity.getTitle()} and ID is ${noteEntity.getId()}", Toast.LENGTH_SHORT).show()
                editResultLauncher.launch(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater : MenuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All data deleted", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)

            }
        }
    }
}