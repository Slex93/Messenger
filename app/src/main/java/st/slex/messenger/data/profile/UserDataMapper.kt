package st.slex.messenger.data.profile

import st.slex.messenger.core.Abstract
import st.slex.messenger.ui.core.UIResult
import st.slex.messenger.ui.user_profile.UserUI

class UserDataMapper : Abstract.Mapper.DataToUi<UserData, UIResult<UserUI>> {

    override fun map(data: UserData): UIResult<UserUI> =
        UIResult.Success(with(data) {
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

    override fun map(exception: Exception): UIResult<UserUI> = UIResult.Failure(exception)
}