package com.example.mvvmexample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

// 여기 잘 봐야한다. RecyclerView.Adapter 대신에 상속받은 ListAdapter를 이용해서 DatasetChanged를 각 아이템의 포지션에 맞게것들을 @DatasetItemDelete 같은 것들로 실행시켜준다.
// 여기서 제일 중요한 사실은 새롭게 상속받은 ListAdapter는 DiffUtil이란 클래스를 가지고 있어서 Thread로 구현을 해주기에 그 이유로 ListAdapter를 사용하는 거다.
// 만약 그렇게 사용하지 않고 지금처럼 사용하면 데이터의 양이 많아지면 아주 속도가 느려지지.. 전체를 다 다시 바꾸니깐..
class NoteAdapter : ListAdapter<NoteEntity, NoteAdapter.NoteHolder>(DIFF_CALLBACK) {
    // 그래 정적 anyonymous 클래스를 인스턴스를 해서 변수에 넣고 그걸 ListAdapter에다가 생성자로 넘겨주었다.
    // 정적으로 만들어야 부모로 생성자가 넘어가기 전에 미리 메모리에 올라가 있으니깐..
    // 여기 이부분 companion object 부분을 이제 조금 더 이해할 것 같다.
    // 그럼 이제 넘어가 DIFF_CALLBACK으로 뭘 할껀데.. 그리고 해당 값이 return인지 확인하는 작업이 필요하잖아..
    // 일단 ListAdapter에서 제공하는 getItem을 사용한다. 기존에는 Notes (ArrayList)를 만들어서 사용했잖아.
    // 그리고 MainActivity에서 onItemchanged에서 adapter.notes(t) 대신에 adapter.submitList(t)를 사용했다. 강력하네.
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NoteEntity>() {
            override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem.getId() == newItem.getId()
            }

            override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem.getTitle().equals(newItem.getTitle()) &&
                       oldItem.getDescription().equals(newItem.getDescription()) &&
                       oldItem.getPriority() == newItem.getPriority()
            }
        }

    }

    // 인터페이스가 필요한 이유는? 근데 내가 알고 있기로 Activity와 관련있는게 아니었고
    // RecyclerView와 Adapter의 관련이 있는 거였지.. 클릭 이벤트에 대한 이유로..
    // 일단 아무것도 없는걸로 초기화를 해놓는게 편하다. 아니면 null을 사용해야 한다.
    // 여기의 notes는 어디서 오늘 것일까?
    private lateinit var listener: OnItemClickListener

    //클릭 이벤트를 만들기 위해서 interface를 사용한다.
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    class NoteHolder : RecyclerView.ViewHolder {
        val textViewTitle : TextView = itemView.findViewById(R.id.text_view_title)
        val textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        val textViewPriority : TextView = itemView.findViewById(R.id.text_view_priority)
        //여기서 내가 넣고 있는 notes와 binding이 되면서 들어가는 notes의 값은 다른걸까?
        constructor(itemView : View, listener: OnItemClickListener) : super(itemView) {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    //Log.e("NoteAdapter", "Position : $position & Title : ${textViewTitle.text} & Notes.get(position).getTitle ${notes.get(position).getTitle()} 입니다." )
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView : View = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent,false)
        //여기서 새로운 노트홀더가 생성되는 곳인데.. 그래서 여기에서 데이터가 들어가는 곳인데..
        return NoteHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        //현재 보여주고자 하는 holder의 위치를 이벤트에서 알아내고 이 position 값을 이용해서 전체 noteEntities에서 혀재 currentNote 값을 알아내고 난 다음
        var currentNote : NoteEntity = getItem(position)
        // 왜 여기에서 헷갈리는 거지? holder가 view이므로 그 안에 값이 textView들이 있어야 하는거잖아...
        holder.textViewTitle.text = currentNote.getTitle().toString()
        Log.e("NoteAdapterOnbind", "Position is $position & currentNote.getTitle().toString is ${currentNote.getTitle().toString()}")
        holder.textViewDescription.text = currentNote.getDescription().toString()
        //String.valueOf
        holder.textViewPriority.text = currentNote.getPriority().toString()
    }

/* ListAdapter를 사용하니깐 이제 이거 의미가 없다. ListAdapter에서 제공해준다.
    override fun getItemCount(): Int {
        return notes.size
    }
*/
/* 이것도 필요가 없다. 이것도 새롭게 바꿀거니깐..
    fun setNotes(notes : List<NoteEntity>) {
        this.notes = notes
        //나중에 바꿀거다.
        notifyDataSetChanged()
    }
*/
    // ********************** position으로 데이터를 주고 받는걸 꼭 기억하자.. 아주 중요한 부분이다. noteEntity를 바로 넘기면 문제가 될 수 있다. ********************
    fun getNoteAt(position : Int) : NoteEntity {
        return getItem(position)
    }
}

