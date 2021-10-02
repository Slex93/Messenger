package st.slex.messenger.data.chats

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.core.DataResult
import st.slex.messenger.utilites.NODE_CHAT_LIST
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ChatsRepository {

    suspend fun getChats(page: Int): Flow<DataResult<List<ChatsData>>>

    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val user: FirebaseUser
    ) : ChatsRepository {
        override suspend fun getChats(page: Int): Flow<DataResult<List<ChatsData>>> =
            callbackFlow {
                val reference = databaseReference
                    .child(NODE_CHAT_LIST)
                    .child(user.uid)
                    .limitToLast(page)

                val listener = getChatsEventListener {
                    trySendBlocking(it)
                }

                reference.addValueEventListener(listener)
                awaitClose { reference.removeEventListener(listener) }
            }

        private inline fun getChatsEventListener(
            crossinline function: (DataResult<List<ChatsData>>) -> Unit
        ) = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) =
                function(
                    DataResult.Success(
                        snapshot.children.mapNotNull {
                            it.getValue(ChatsData.Base::class.java)!!
                        })
                )

            override fun onCancelled(error: DatabaseError) =
                function(DataResult.Failure(error.toException()))
        }

    }
}