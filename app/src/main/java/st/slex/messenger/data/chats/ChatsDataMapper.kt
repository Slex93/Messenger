package st.slex.messenger.data.chats

import st.slex.messenger.core.Abstract
import st.slex.messenger.ui.chats.ChatsUI
import st.slex.messenger.ui.core.UIResult

class ChatsDataMapper : Abstract.Mapper.DataToUi<List<ChatsData>, UIResult<List<ChatsUI>>> {

    override fun map(data: List<ChatsData>): UIResult<List<ChatsUI>> =
        UIResult.Success(data.map {
            ChatsUI.Base(
                id = it.chatId(),
                username = it.username(),
                text = it.text(),
                url = it.url(),
                timestamp = it.timestamp()
            )
        })

    override fun map(exception: Exception): UIResult<List<ChatsUI>> = UIResult.Failure(exception)

}