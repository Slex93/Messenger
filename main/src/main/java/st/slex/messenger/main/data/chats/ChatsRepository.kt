package st.slex.messenger.main.data.chats

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.FirebaseConstants.NODE_CHATS
import st.slex.messenger.core.Resource
import st.slex.messenger.main.data.core.ValueSnapshotListener
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ChatsRepository {

    suspend fun getAllChats(): Flow<Resource<List<ChatsData>>>

    class Base @Inject constructor(
        reference: DatabaseReference,
        user: FirebaseUser,
        private val valueListener: ValueSnapshotListener
    ) : ChatsRepository {

        private val chatsReference: DatabaseReference = reference.child(NODE_CHATS).child(user.uid)

        override suspend fun getAllChats(): Flow<Resource<List<ChatsData>>> = callbackFlow {
            val listener = valueListener.multipleEventListener(ChatsData.Base::class) {
                trySendBlocking(it)
            }
            chatsReference.addValueEventListener(listener)
            awaitClose { chatsReference.removeEventListener(listener) }
        }
    }
}