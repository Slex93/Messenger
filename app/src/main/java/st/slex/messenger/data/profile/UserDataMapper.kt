package st.slex.messenger.data.profile

import st.slex.messenger.core.Abstract
import st.slex.messenger.domain.user.UserDomain
import st.slex.messenger.domain.user.UserDomainResult

interface UserDataMapper<T> : Abstract.Mapper.DataToDomain<UserData, T> {

    class Base : UserDataMapper<UserDomainResult> {
        override fun map(data: UserData): UserDomainResult =
            UserDomainResult.Success(with(data) {
                UserDomain.Base(
                    id = id(),
                    phone = phone(),
                    username = username(),
                    url = url(),
                    bio = bio(),
                    full_name = fullName(),
                    state = state(),
                )
            })

        override fun map(exception: Exception): UserDomainResult =
            UserDomainResult.Failure(exception)

    }
}