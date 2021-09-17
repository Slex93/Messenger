package st.slex.messenger.domain.user

import st.slex.messenger.core.Abstract
import st.slex.messenger.ui.user_profile.UserUI
import st.slex.messenger.ui.user_profile.UserUiResult

interface UserDomainMapper<T> : Abstract.Mapper.DomainToUi<UserDomain, T> {

    class Base : UserDomainMapper<UserUiResult> {
        override fun map(data: UserDomain): UserUiResult =
            UserUiResult.Success(with(data) {
                UserUI.Base(
                    id = id(),
                    phone = phone(),
                    username = username(),
                    url = url(),
                    bio = bio(),
                    full_name = fullName(),
                    state = state(),
                )
            })

        override fun map(exception: Exception): UserUiResult =
            UserUiResult.Failure(exception)

    }
}