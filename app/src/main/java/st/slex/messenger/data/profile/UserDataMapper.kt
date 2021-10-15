package st.slex.messenger.data.profile

import st.slex.messenger.core.Mapper
import st.slex.messenger.core.Resource
import st.slex.messenger.ui.user_profile.UserUI
import javax.inject.Inject

class UserDataMapper @Inject constructor() : Mapper.ToUI<UserData, Resource<UserUI>> {

    override fun map(data: UserData?): Resource<UserUI> =
        Resource.Success(data?.let {
            with(it) {
                UserUI.Base(
                    id = getId,
                    phone = getPhone,
                    username = getUsername,
                    url = getUrl,
                    bio = getBio,
                    full_name = getFullName,
                    state = getState,
                )
            }
        })

    override fun map(exception: Exception): Resource<UserUI> = Resource.Failure(exception)

    override fun map(data: Nothing?): Resource<UserUI> = Resource.Loading()
}