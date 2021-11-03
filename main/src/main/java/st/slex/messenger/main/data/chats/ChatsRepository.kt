package st.slex.messenger.main.data.chats

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.FirebaseConstants.NODE_CHATS
import st.slex.messenger.core.Resource
import st.slex.messenger.main.data.core.ValueSnapshotListener

@ExperimentalCoroutinesApi
interface ChatsRepository {

    suspend fun getAllChats(): Flow<Resource<List<ChatsData>>>

    class Base @AssistedInject constructor(
        reference: DatabaseReference,
        user: FirebaseUser,
        private val valueListener: ValueSnapshotListener,
        @Assisted("page_number") pageNumber: Int
    ) : ChatsRepository {

        private val chatsReference: Query = reference
            .child(NODE_CHATS)
            .child(user.uid)
            .limitToLast(pageNumber)

        override suspend fun getAllChats(): Flow<Resource<List<ChatsData>>> = callbackFlow {
            val listener = valueListener.multipleEventListener(ChatsData.Base::class) {
                trySendBlocking(it)
            }
            chatsReference.addValueEventListener(listener)
            awaitClose { chatsReference.removeEventListener(listener) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("page_number") pageNumber: Int): Base
    }
}