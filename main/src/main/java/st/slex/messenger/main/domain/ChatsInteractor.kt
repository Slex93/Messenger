package st.slex.messenger.main.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import st.slex.messenger.core.Resource
import st.slex.messenger.main.data.chats.ChatsDataMapper
import st.slex.messenger.main.data.contacts.ContactsRepository
import st.slex.messenger.main.data.user.UserRepository
import st.slex.messenger.main.ui.chats.ChatsUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ChatsInteractor {

    suspend fun getAllChats(pageNumber: Int): Flow<Resource<List<Resource<ChatsUI>>>>

    class Base @Inject constructor(
        private val userRepository: UserRepository,
        private val contactsRepository: ContactsRepository,
        private val chatsUseCase: ChatsUseCase,
        private val mapper: ChatsDataMapper
    ) : ChatsInteractor {

        override suspend fun getAllChats(pageNumber: Int): Flow<Resource<List<Resource<ChatsUI>>>> =
            flow {
                chatsUseCase.invoke(pageNumber).getAllChats().collect { allChatsResult ->
                    when (val result = allChatsResult.map(mapper)) {
                        is Resource.Success -> {
                            val mappedChats = result.data.map { chatsUI ->
                                getChatUIHead(chatsUI).first()
                            }
                            emit(Resource.Success(mappedChats))
                        }
                        is Resource.Failure -> emit(Resource.Failure<Nothing>(result.exception))
                        is Resource.Loading -> emit(Resource.Loading)
                    }
                }
            }

        private suspend fun getChatUIHead(chat: ChatsUI): Flow<Resource<ChatsUI>> =
            getUserUrl(chat.gettingId()).combine(getUserName(chat.gettingId())) { resUrl, resName ->
                mediatorForCheckingResult(resName = resName, resUrl = resUrl, chat = chat)
            }

        private suspend fun getUserUrl(id: String): Flow<Resource<String>> =
            userRepository.getUserUrl(id)

        private suspend fun getUserName(id: String): Flow<Resource<String>> =
            contactsRepository.getContactFullName(id)

        private suspend fun mediatorForCheckingResult(
            resName: Resource<String>,
            resUrl: Resource<String>,
            chat: ChatsUI
        ): Resource<ChatsUI> = withContext(Dispatchers.Default) {
            return@withContext when (val checkedFullNameResult =
                fullNameResultChecker(resName, chat)) {
                is Resource.Success -> urlResultChecker(resUrl, checkedFullNameResult.data)
                is Resource.Failure -> Resource.Failure(checkedFullNameResult.exception)
                is Resource.Loading -> Resource.Loading
            }
        }

        private fun fullNameResultChecker(
            result: Resource<String>,
            chat: ChatsUI
        ): Resource<ChatsUI> = when (result) {
            is Resource.Success -> Resource.Success(chat.copy(full_name = result.data))
            is Resource.Failure -> Resource.Success(chat.copy(full_name = chat.gettingId()))
            is Resource.Loading -> Resource.Loading
        }

        private fun urlResultChecker(
            result: Resource<String>,
            chat: ChatsUI
        ): Resource<ChatsUI> = when (result) {
            is Resource.Success -> Resource.Success(chat.copy(url = result.data))
            is Resource.Failure -> Resource.Success(chat)
            is Resource.Loading -> Resource.Loading
        }
    }
}