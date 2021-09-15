package st.slex.messenger.data.chats

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.AppValueEventListener
import st.slex.messenger.utilites.NODE_CHAT_LIST
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ChatsRepository {

    suspend fun getChats(page: Int): Flow<List<ChatsData.Base?>>

    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val user: FirebaseUser
    ) : ChatsRepository {

        override suspend fun getChats(page: Int): Flow<List<ChatsData.Base?>> = callbackFlow {
            val reference = databaseReference
                .child(NODE_CHAT_LIST)
                .child(user.uid)
                .limitToLast(page)
            val listener = AppValueEventListener({ snapshot ->
                val result = snapshot.children.map { it.getValue(ChatsData.Base::class.java) }
                trySendBlocking(result)
            }, { _ ->
            })
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }
    }
}