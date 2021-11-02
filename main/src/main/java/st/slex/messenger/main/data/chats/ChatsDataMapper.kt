package st.slex.messenger.main.data.chats

import st.slex.messenger.core.Mapper
import st.slex.messenger.core.Resource
import st.slex.messenger.main.ui.chats.ChatsUI
import javax.inject.Inject

interface ChatsDataMapper : Mapper.ToUI<List<ChatsData>, Resource<List<ChatsUI>>> {

    class Base @Inject constructor() : ChatsDataMapper {

        override fun map(data: List<ChatsData>): Resource<List<ChatsUI>> =
            Resource.Success(data.map {
                ChatsUI.Base(
                    id = it.chatId(),
                    from = it.from(),
                    full_name = it.username(),
                    message = it.text(),
                    timestamp = it.timestamp()
                )
            })

        override fun map(exception: Exception): Resource<List<ChatsUI>> =
            Resource.Failure(exception)

        override fun map(): Resource<List<ChatsUI>> = Resource.Loading
    }
}