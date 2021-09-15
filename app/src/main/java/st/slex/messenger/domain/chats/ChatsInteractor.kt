package st.slex.messenger.domain.chats

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.core.TestResponse
import st.slex.messenger.data.chats.ChatsData
import st.slex.messenger.data.chats.ChatsRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ChatsInteractor {
    fun getChatsList(page: Int): Flow<TestResponse<List<ChatsDomain>>>
    fun getResult(data: List<ChatsData>): List<ChatsDomain>
    class Base @Inject constructor(
        private val repository: ChatsRepository,
        private val mapper: ChatsData.ChatsDataMapper<ChatsDomain>
    ) : ChatsInteractor {
        override fun getChatsList(page: Int): Flow<TestResponse<List<ChatsDomain>>> = callbackFlow {
            try {
                repository.getChats(page).collect {
                    when (it) {
                        is TestResponse.Success -> {
                            trySendBlocking(TestResponse.Success(getResult(it.value)))
                        }
                        is TestResponse.Failure -> {
                            trySendBlocking(TestResponse.Failure(it.exception))
                        }
                        else -> {
                        }
                    }
                }
            } catch (exception: Exception) {
                trySendBlocking(TestResponse.Failure(exception))
            }
            awaitClose { }
        }

        override fun getResult(data: List<ChatsData>) = data.map {
            it.map(mapper)
        }


    }
}