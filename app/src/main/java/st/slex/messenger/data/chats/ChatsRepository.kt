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
import st.slex.messenger.utilites.NODE_CHAT_LIST
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ChatsRepository {

    suspend fun getChats(page: Int): Flow<ChatsDataResult>

    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val user: FirebaseUser
    ) : ChatsRepository {
        override suspend fun getChats(page: Int): Flow<ChatsDataResult> =
            callbackFlow {
                val reference = databaseReference
                    .child(NODE_CHAT_LIST)
                    .child(user.uid)
                    .limitToLast(page)

                val listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val result = snapshot.children.mapNotNull {
                            it.getValue(ChatsData.Base::class.java)!!
                        }
                        trySendBlocking(ChatsDataResult.Success(result))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        trySendBlocking(ChatsDataResult.Failure(error.toException()))
                    }

                }

                reference.addValueEventListener(listener)
                awaitClose { reference.removeEventListener(listener) }
            }

    }
}