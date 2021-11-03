package st.slex.messenger.main.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.main.data.chats.ChatsRepository
import javax.inject.Inject


@ExperimentalCoroutinesApi
class ChatsUseCase @Inject constructor(
    private val repository: ChatsRepository.Factory
) {

    fun invoke(pageNumber: Int): ChatsRepository = repository.create(pageNumber)
}