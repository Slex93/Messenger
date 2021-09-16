package st.slex.messenger.domain.chats

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.chats.ChatsDataMapper
import st.slex.messenger.data.chats.ChatsRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ChatsInteractor {
    fun getChatsList(page: Int): Flow<ChatsDomainResult>
    class Base @Inject constructor(
        private val repository: ChatsRepository,
        private val mapper: ChatsDataMapper<ChatsDomainResult>
    ) : ChatsInteractor {
        override fun getChatsList(page: Int): Flow<ChatsDomainResult> = callbackFlow {
            try {
                repository.getChats(page).collect {
                    trySendBlocking(it.map(mapper))
                }
            } catch (exception: Exception) {
                trySendBlocking(ChatsDomainResult.Failure(exception))
            }
            awaitClose { }
        }
    }
}