package st.slex.messenger.data.user

import st.slex.messenger.core.Mapper
import st.slex.messenger.core.Resource
import st.slex.messenger.ui.user_profile.UserUI
import javax.inject.Inject

class UserDataMapper @Inject constructor() : Mapper.ToUI<UserData, Resource<UserUI>> {

    override fun map(data: UserData): Resource<UserUI> =
        Resource.Success(with(data) {
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

    override fun map(exception: Exception): Resource<UserUI> = Resource.Failure(exception)

    override fun map(): Resource<UserUI> = Resource.Loading
}