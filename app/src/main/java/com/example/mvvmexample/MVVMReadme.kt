package com.example.mvvmexample

fun main() {

}
/*
ROOM, Entities, DAO, Database, ModelView, RecyclerView, LiveData
* Activity와 Fragment안에 있는
  - View들이 여전히 Entity와 관련이 있는데 그러면 Entity들에 맞춰서 작업을 해주면 된다는 건가?
  - 다른 컴포넌트들이 여전히 많은데 그런건 다 어디에서 연결해서 해주는거지? 그리고 나중에 ViewModel이 바뀌면 거기에 따라 컴포넌트들을 바꿔줘야 하는건가?
* ViewModel
  - 말그대로 View와 Model 사이를 정의해 놓은건 아닐까?
  - 아마도 여기가 제일 중요할 것 같은데..
* Repository
  - 저장소라는 뜻이잖아. 그 저장소는 여러곳이 될 수 있는데 그런 곳들을 실제로 연결하기 위한 환경을 만들어 놓는다는 거겠지?
  - 여기에 환경에 따라 변환된다는 거잖아.
* Room
  - 데이터베이스를 실제로 연결하기 위한 코드들인데 그전에 알듯이 DAO가 있고 SQLite가 있고 Entity가 있고 그런걸 Room이 실시간으로
  - 데이터를 SQL로 업데이트 한다는 거잖아..
  - 이때 Room이 작동하게 끔 만드는게 어떤식으로 되는건지 알아야 된다는 거지..
* LiveData가 작동되는 방식..
 */