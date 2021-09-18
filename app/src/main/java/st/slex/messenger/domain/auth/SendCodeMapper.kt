package st.slex.messenger.domain.auth

import st.slex.messenger.core.Abstract
import st.slex.messenger.ui.auth.SendCodeUIResult

interface SendCodeMapper<T> : Abstract.Mapper.DomainToUi<UserInitial, T> {

    class Base : SendCodeMapper<SendCodeUIResult> {
        override fun map(data: UserInitial): SendCodeUIResult =
            SendCodeUIResult.Success(data)

        override fun map(exception: Exception): SendCodeUIResult =
            SendCodeUIResult.Failure(exception)
    }
}