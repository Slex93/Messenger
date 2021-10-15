package st.slex.messenger.data.chats

import st.slex.messenger.core.Mapper
import st.slex.messenger.core.Resource
import st.slex.messenger.ui.chats.ChatsUI
import javax.inject.Inject

class ChatsDataMapper @Inject constructor() :
    Mapper.ToUI<List<ChatsData>, Resource<List<ChatsUI>>> {

    override fun map(data: List<ChatsData>?): Resource<List<ChatsUI>> =
        Resource.Success(data?.map {
            ChatsUI.Base(
                id = it.chatId(),
                username = it.username(),
                text = it.text(),
                url = it.url(),
                timestamp = it.timestamp()
            )
        })

    override fun map(exception: Exception): Resource<List<ChatsUI>> = Resource.Failure(exception)

    override fun map(data: Nothing?): Resource<List<ChatsUI>> = Resource.Loading()
}